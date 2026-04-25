package report;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import report.types.MonthlyStore;
import report.types.RunResult;
import utils.logger.LoggingUtil;

public class GitHubStore {

    private static final String API = "https://api.github.com";
    private static final String OWNER = env("DATA_REPO_OWNER", "Phillmon8917");
    private static final String REPO = env("DATA_REPO_NAME", "phptravels-reports");
    private static final String BRANCH = env("DATA_REPO_BRANCH", "java-reports");
    private static final String TOKEN = env("DATA_REPO_TOKEN", "");
    private static final String KNOWN_IDS_PATH = "data/known-test-ids.json";

    /**
     * How many times to retry appendRun on a 409 conflict before giving up.
     */
    private static final int MAX_RETRIES = 5;

    /**
     * Base delay (ms) for exponential back-off between conflict retries.
     */
    private static final long RETRY_BASE_DELAY_MS = 500;

    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Returns the environment variable value for the supplied key or a fallback
     * when it is blank.
     *
     * @param key the environment variable name to read
     * @param fallback the fallback value to use when the variable is missing
     * @return the resolved environment value
     */
    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : fallback;
    }

    /**
     * Builds the GitHub API request headers required for authenticated
     * repository access.
     *
     * @return the request headers used for GitHub API calls
     */
    private static Map<String, String> headers() {
        if (TOKEN.isBlank()) {
            throw new IllegalStateException(
                    "[github-store] DATA_REPO_TOKEN is not set");
        }
        return Map.of(
                "Authorization", "Bearer " + TOKEN,
                "Accept", "application/vnd.github+json",
                "X-GitHub-Api-Version", "2022-11-28",
                "Content-Type", "application/json"
        );
    }

    /**
     * Returns the repository data path for a monthly report file.
     *
     * @param month the month key in {@code yyyy-MM} format
     * @return the repository-relative path for the month file
     */
    private static String dataPath(String month) {
        return "data/" + month + ".json";
    }

    public record StoreWithSha(MonthlyStore store, String sha) {

    }

    /**
     * Loads a monthly store and its blob SHA from the GitHub data repository.
     *
     * @param month the month key in {@code yyyy-MM} format
     * @return the loaded store together with its SHA, or an empty store when no
     * file exists
     * @throws Exception if the store cannot be fetched or parsed
     */
    public static StoreWithSha loadStore(String month) throws Exception {
        String url = API + "/repos/" + OWNER + "/" + REPO
                + "/contents/" + dataPath(month) + "?ref=" + BRANCH;

        HttpResponse<String> res = HTTP.send(buildGet(url),
                HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() == 404) {
            LoggingUtil.info("[github-store] " + month + ".json not found — will create on first write");
            return new StoreWithSha(new MonthlyStore(month, new ArrayList<>()), null);
        }
        assertOk(res, "fetch " + month + ".json");

        Map<?, ?> json = MAPPER.readValue(res.body(), Map.class);
        String rawContent = new String(Base64.getMimeDecoder()
                .decode((String) json.get("content")));
        MonthlyStore store = MAPPER.readValue(rawContent, MonthlyStore.class);

        LoggingUtil.info("[github-store] Loaded " + month + ".json — runs: " + store.runs().size());
        return new StoreWithSha(store, (String) json.get("sha"));
    }

    /**
     * Saves a monthly store to the GitHub data repository.
     *
     * @param store the monthly store to save
     * @param sha the current blob SHA when updating an existing file, or
     * {@code null} for a new file
     * @throws Exception if the store cannot be serialized or uploaded
     */
    public static void saveStore(MonthlyStore store, String sha) throws Exception {
        String url = API + "/repos/" + OWNER + "/" + REPO
                + "/contents/" + dataPath(store.month());

        String content = Base64.getEncoder().encodeToString(
                MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(store));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "ci: append run to " + store.month() + ".json [skip ci]");
        body.put("content", content);
        body.put("branch", BRANCH);
        if (sha != null) {
            body.put("sha", sha);
        }

        HttpResponse<String> res = HTTP.send(buildPut(url, MAPPER.writeValueAsString(body)),
                HttpResponse.BodyHandlers.ofString());
        assertOk(res, "save " + store.month() + ".json");
        LoggingUtil.info("[github-store] Saved " + store.month() + ".json to repo");
    }

    /**
     * Appends a run result to the monthly store in the GitHub data repository.
     *
     * <p>
     * Uses an optimistic-concurrency retry loop to handle the case where two
     * parallel CI shards attempt to write at the same time. On a 409 conflict
     * the method re-fetches the latest file (and its up-to-date SHA), appends
     * the run again, and retries the PUT, backing off exponentially between
     * attempts.
     *
     * @param run the run result to append
     * @throws Exception if the run cannot be persisted after all retries are
     * exhausted
     */
    public static void appendRun(RunResult run) throws Exception {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            StoreWithSha loaded = loadStore(run.month());

            // Deduplicate: skip if this runId was already written by a concurrent shard.
            boolean alreadyPresent = loaded.store().runs().stream()
                    .anyMatch(r -> r.runId().equals(run.runId()));
            if (alreadyPresent) {
                LoggingUtil.info("[github-store] Run " + run.runId()
                        + " already present in " + run.month() + ".json — skipping duplicate write");
                return;
            }

            List<RunResult> runs = new ArrayList<>(loaded.store().runs());
            runs.add(run);
            MonthlyStore updated = new MonthlyStore(run.month(), runs);

            try {
                saveStore(updated, loaded.sha());
                LoggingUtil.info("[github-store] Run " + run.runId()
                        + " appended to " + run.month() + ".json (attempt " + attempt + ")");
                return; // success — exit the retry loop

            } catch (RuntimeException e) {
                boolean isConflict = e.getMessage() != null
                        && (e.getMessage().contains("409")
                        || e.getMessage().contains("conflict"));

                if (!isConflict || attempt == MAX_RETRIES) {
                    throw e; // non-conflict error, or retries exhausted — propagate
                }

                long delay = RETRY_BASE_DELAY_MS * (1L << (attempt - 1)); // 500, 1000, 2000, 4000 ms
                LoggingUtil.info("[github-store] 409 conflict on attempt " + attempt
                        + " — retrying in " + delay + " ms");
                Thread.sleep(delay);
            }
        }
    }

    public record ReportData(String reportMonth, MonthlyStore reportStore,
            String currentMonth, MonthlyStore currentStore) {

    }

    /**
     * Loads the previous month and current month report data from the GitHub
     * data repository.
     *
     * @return the report data bundle for report generation
     * @throws Exception if the data cannot be fetched
     */
    public static ReportData loadReportData() throws Exception {
        Calendar now = Calendar.getInstance();
        String currentMonth = String.format("%d-%02d",
                now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);

        Calendar prev = (Calendar) now.clone();
        prev.add(Calendar.MONTH, -1);
        String reportMonth = String.format("%d-%02d",
                prev.get(Calendar.YEAR), prev.get(Calendar.MONTH) + 1);

        LoggingUtil.info("[github-store] Loading report data — reportMonth: "
                + reportMonth + ", currentMonth: " + currentMonth);

        MonthlyStore reportStore = loadStore(reportMonth).store();
        MonthlyStore currentStore = loadStore(currentMonth).store();

        LoggingUtil.info("[github-store] reportStore runs: " + reportStore.runs().size()
                + ", currentStore runs: " + currentStore.runs().size());

        return new ReportData(reportMonth, reportStore, currentMonth, currentStore);
    }

    /**
     * Loads the set of known test identifiers from the GitHub data repository.
     *
     * @return the set of known test identifiers
     * @throws Exception if the identifiers cannot be fetched or parsed
     */
    public static Set<String> loadKnownTestIds() throws Exception {
        String url = API + "/repos/" + OWNER + "/" + REPO
                + "/contents/" + KNOWN_IDS_PATH + "?ref=" + BRANCH;

        HttpResponse<String> res = HTTP.send(buildGet(url),
                HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() == 404) {
            LoggingUtil.info("[github-store] known-test-ids.json not found — all tests treated as new");
            return new HashSet<>();
        }
        assertOk(res, "fetch known-test-ids.json");

        Map<?, ?> json = MAPPER.readValue(res.body(), Map.class);
        String rawContent = new String(Base64.getMimeDecoder()
                .decode((String) json.get("content")));
        List<String> ids = MAPPER.readValue(rawContent, List.class);

        LoggingUtil.info("[github-store] Loaded " + ids.size() + " known test IDs");
        return new HashSet<>(ids);
    }

    /**
     * Saves the supplied known test identifiers to the GitHub data repository.
     *
     * @param ids the test identifiers to persist
     * @throws Exception if the identifiers cannot be serialized or uploaded
     */
    public static void saveKnownTestIds(Set<String> ids) throws Exception {
        String url = API + "/repos/" + OWNER + "/" + REPO
                + "/contents/" + KNOWN_IDS_PATH;

        String sha = null;
        HttpResponse<String> getRes = HTTP.send(
                buildGet(url + "?ref=" + BRANCH),
                HttpResponse.BodyHandlers.ofString());
        if (getRes.statusCode() == 200) {
            sha = (String) MAPPER.readValue(getRes.body(), Map.class).get("sha");
        }

        String content = Base64.getEncoder().encodeToString(
                MAPPER.writerWithDefaultPrettyPrinter()
                        .writeValueAsBytes(new ArrayList<>(ids)));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "ci: update known-test-ids.json [skip ci]");
        body.put("content", content);
        body.put("branch", BRANCH);
        if (sha != null) {
            body.put("sha", sha);
        }

        HttpResponse<String> res = HTTP.send(
                buildPut(url, MAPPER.writeValueAsString(body)),
                HttpResponse.BodyHandlers.ofString());
        assertOk(res, "save known-test-ids.json");
        LoggingUtil.info("[github-store] Saved " + ids.size() + " known test IDs");
    }

    /**
     * Builds an authenticated HTTP GET request for the supplied GitHub API URL.
     *
     * @param url the target request URL
     * @return the configured HTTP GET request
     */
    private static HttpRequest buildGet(String url) {
        var b = HttpRequest.newBuilder().uri(URI.create(url)).GET();
        headers().forEach(b::header);
        return b.build();
    }

    /**
     * Builds an authenticated HTTP PUT request for the supplied GitHub API URL
     * and request body.
     *
     * @param url the target request URL
     * @param body the JSON request body to send
     * @return the configured HTTP PUT request
     */
    private static HttpRequest buildPut(String url, String body) {
        var b = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(body));
        headers().forEach(b::header);
        return b.build();
    }

    /**
     * Validates that a GitHub API response completed successfully.
     *
     * @param res the HTTP response to validate
     * @param ctx a short description of the operation being validated
     */
    private static void assertOk(HttpResponse<String> res, String ctx) {
        if (res.statusCode() < 200 || res.statusCode() >= 300) {
            throw new RuntimeException("[github-store] Failed to " + ctx
                    + ": " + res.statusCode() + "\n" + res.body());
        }
    }
}
