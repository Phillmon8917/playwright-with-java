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
     * How many times to retry a write operation on a 409 conflict before giving
     * up.
     */
    private static final int MAX_RETRIES = 5;

    /**
     * Base delay in milliseconds for exponential back-off between conflict
     * retries.
     */
    private static final long RETRY_BASE_DELAY_MS = 500;

    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Returns the value of the given environment variable, or a fallback value
     * when the variable is absent or blank.
     *
     * @param key the name of the environment variable to read
     * @param fallback the value to return when the variable is missing or blank
     * @return the resolved environment value, or {@code fallback} when unset
     */
    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : fallback;
    }

    /**
     * Builds the authenticated HTTP request headers required for GitHub API
     * access.
     *
     * @return an immutable map of header name-to-value pairs for GitHub API
     * calls
     * @throws IllegalStateException when {@code DATA_REPO_TOKEN} is blank
     */
    private static Map<String, String> headers() {
        if (TOKEN.isBlank()) {
            throw new IllegalStateException("[github-store] DATA_REPO_TOKEN is not set");
        }
        return Map.of(
                "Authorization", "Bearer " + TOKEN,
                "Accept", "application/vnd.github+json",
                "X-GitHub-Api-Version", "2022-11-28",
                "Content-Type", "application/json"
        );
    }

    /**
     * Returns the repository-relative path for a monthly report file.
     *
     * @param month the month key in {@code yyyy-MM} format
     * @return the path string used when reading or writing the month file
     */
    private static String dataPath(String month) {
        return "data/" + month + ".json";
    }

    /**
     * A pairing of a {@link MonthlyStore} with the blob SHA returned by the
     * GitHub Contents API.
     *
     * @param store the deserialized monthly store
     * @param sha the current blob SHA of the file, or {@code null} when the
     * file does not yet exist in the repository
     */
    public record StoreWithSha(MonthlyStore store, String sha) {

    }

    /**
     * Loads a monthly store and its current blob SHA from the GitHub data
     * repository. Returns an empty store when the file does not yet exist.
     *
     * @param month the month key in {@code yyyy-MM} format
     * @return the loaded {@link StoreWithSha}, or an empty store when the file
     * does not exist
     * @throws Exception if the file cannot be fetched or its content cannot be
     * parsed
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
     * Loads a monthly store filtered to only runs of the given type.
     * <p>
     * Runs whose {@code runType} is {@code null} or blank are treated as
     * {@code "monthly"} for backwards-compatibility with data written before
     * the {@code runType} field was introduced.
     *
     * @param month the month key in {@code yyyy-MM} format
     * @param runType the run type to keep — all other run types are excluded
     * @return a {@link MonthlyStore} containing only the matching runs
     * @throws Exception if the file cannot be fetched or parsed
     */
    public static MonthlyStore loadStoreFiltered(String month, String runType) throws Exception {
        MonthlyStore full = loadStore(month).store();
        List<RunResult> filtered = full.runs().stream()
                .filter(r -> {
                    String t = r.runType();
                    // Runs written before runType was added default to "monthly".
                    String effective = (t == null || t.isBlank()) ? MonthlyReporter.TYPE_MONTHLY : t;
                    return effective.equals(runType);
                })
                .toList();
        LoggingUtil.info("[github-store] " + month + ".json filtered to runType='"
                + runType + "' — " + filtered.size() + " of " + full.runs().size() + " runs kept");
        return new MonthlyStore(month, filtered);
    }

    /**
     * Writes a monthly store to the GitHub data repository.
     *
     * @param store the monthly store to serialize and upload
     * @param sha the current blob SHA when updating, or {@code null} when
     * creating
     * @throws Exception if the store cannot be serialized or the PUT request
     * fails
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
     * Uses an optimistic-concurrency retry loop to handle parallel CI shards.
     *
     * @param run the run result to append to the monthly store
     * @throws Exception if the run cannot be persisted after all retries are
     * exhausted
     */
    public static void appendRun(RunResult run) throws Exception {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            StoreWithSha loaded = loadStore(run.month());

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
                return;

            } catch (RuntimeException e) {
                boolean isConflict = e.getMessage() != null
                        && (e.getMessage().contains("409")
                        || e.getMessage().contains("conflict"));

                if (!isConflict || attempt == MAX_RETRIES) {
                    throw e;
                }

                long delay = RETRY_BASE_DELAY_MS * (1L << (attempt - 1));
                LoggingUtil.info("[github-store] 409 conflict on attempt " + attempt
                        + " — retrying in " + delay + " ms");
                Thread.sleep(delay);
            }
        }
    }

    /**
     * Bundle of monthly store data used for report generation.
     *
     * @param reportMonth the month key for the report period (previous month)
     * @param reportStore the {@link MonthlyStore} for the report period
     * @param currentMonth the month key for the current month
     * @param currentStore the {@link MonthlyStore} for the current month
     */
    public record ReportData(String reportMonth, MonthlyStore reportStore,
            String currentMonth, MonthlyStore currentStore) {

    }

    /**
     * Loads report data for the previous and current months, filtered to
     * {@code "monthly"} runs only.
     * <p>
     * Regression runs stored in the same JSON files are excluded here so the
     * monthly report never shows regression data.
     *
     * @return a {@link ReportData} bundle containing only monthly-typed runs
     * @throws Exception if either monthly file cannot be fetched or parsed
     */
    public static ReportData loadReportData() throws Exception {
        Calendar now = Calendar.getInstance();
        String currentMonth = String.format("%d-%02d",
                now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);

        Calendar prev = (Calendar) now.clone();
        prev.add(Calendar.MONTH, -1);
        String reportMonth = String.format("%d-%02d",
                prev.get(Calendar.YEAR), prev.get(Calendar.MONTH) + 1);

        LoggingUtil.info("[github-store] Loading monthly report data — reportMonth: "
                + reportMonth + ", currentMonth: " + currentMonth);

        // Filter to monthly runs only — regression runs are excluded.
        MonthlyStore reportStore = loadStoreFiltered(reportMonth, MonthlyReporter.TYPE_MONTHLY);
        MonthlyStore currentStore = loadStoreFiltered(currentMonth, MonthlyReporter.TYPE_MONTHLY);

        LoggingUtil.info("[github-store] reportStore monthly runs: " + reportStore.runs().size()
                + ", currentStore monthly runs: " + currentStore.runs().size());

        return new ReportData(reportMonth, reportStore, currentMonth, currentStore);
    }

    /**
     * Loads the set of known test identifiers from the GitHub data repository.
     *
     * @return the set of known test identifiers, or an empty set when the file
     * does not exist
     * @throws Exception if the file cannot be fetched or its content cannot be
     * parsed
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
     * Saves the supplied set of known test identifiers to the GitHub data
     * repository. Uses an optimistic-concurrency retry loop to handle parallel
     * CI shards.
     *
     * @param ids the complete set of test identifiers to persist
     * @throws Exception if the identifiers cannot be uploaded after all retries
     * are exhausted
     */
    public static void saveKnownTestIds(Set<String> ids) throws Exception {
        String url = API + "/repos/" + OWNER + "/" + REPO
                + "/contents/" + KNOWN_IDS_PATH;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
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

            try {
                assertOk(res, "save known-test-ids.json");
                LoggingUtil.info("[github-store] Saved " + ids.size() + " known test IDs");
                return;

            } catch (RuntimeException e) {
                boolean isConflict = e.getMessage() != null
                        && (e.getMessage().contains("409")
                        || e.getMessage().contains("conflict"));

                if (!isConflict || attempt == MAX_RETRIES) {
                    throw e;
                }

                long delay = RETRY_BASE_DELAY_MS * (1L << (attempt - 1));
                LoggingUtil.info("[github-store] 409 conflict on known-test-ids attempt " + attempt
                        + " — retrying in " + delay + " ms");
                Thread.sleep(delay);
            }
        }
    }

    /**
     * Builds an authenticated HTTP GET request targeting the given GitHub API
     * URL.
     *
     * @param url the fully-qualified URL to request
     * @return a configured {@link HttpRequest} ready to be sent
     */
    private static HttpRequest buildGet(String url) {
        var b = HttpRequest.newBuilder().uri(URI.create(url)).GET();
        headers().forEach(b::header);
        return b.build();
    }

    /**
     * Builds an authenticated HTTP PUT request with the supplied JSON body.
     *
     * @param url the fully-qualified URL to request
     * @param body the JSON-encoded request body
     * @return a configured {@link HttpRequest} ready to be sent
     */
    private static HttpRequest buildPut(String url, String body) {
        var b = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .PUT(HttpRequest.BodyPublishers.ofString(body));
        headers().forEach(b::header);
        return b.build();
    }

    /**
     * Asserts that the given HTTP response indicates success (status 2xx).
     *
     * @param res the HTTP response to validate
     * @param ctx a short description of the operation, used in the exception
     * message
     * @throws RuntimeException when the response status is not in the 2xx range
     */
    private static void assertOk(HttpResponse<String> res, String ctx) {
        if (res.statusCode() < 200 || res.statusCode() >= 300) {
            throw new RuntimeException("[github-store] Failed to " + ctx
                    + ": " + res.statusCode() + "\n" + res.body());
        }
    }
}
