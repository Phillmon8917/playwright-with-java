package report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import report.types.MonthlyStore;
import report.types.RunResult;
import utils.logger.LoggingUtil;

public class Store {

    private static final ObjectMapper MAPPER   = new ObjectMapper();
    private static final Path         STORE_DIR = Path.of("data");
    private static final String       KNOWN_IDS = "known-test-ids.json";

    /**
     * Determines whether the current execution is running inside GitHub Actions.
     *
     * @return {@code true} when the process is running in GitHub Actions; otherwise {@code false}
     */
    private static boolean isCI() {
        return "true".equals(System.getenv("GITHUB_ACTIONS"));
    }

    /**
     * Appends a run result to the active monthly store using the CI-backed or local store implementation.
     *
     * @param run the run result to append
     * @throws Exception if the run cannot be persisted
     */
    public static void appendRun(RunResult run) throws Exception {
        if (isCI()) { GitHubStore.appendRun(run); return; }

        MonthlyStore store = localLoad(run.month());
        List<RunResult> runs = new ArrayList<>(store.runs());
        runs.add(run);
        localSave(new MonthlyStore(run.month(), runs));
        pruneOldFiles(run.month());
        LoggingUtil.info("[store] Run " + run.runId() + " saved locally to " + run.month() + ".json");
    }

    public record ReportData(String reportMonth, MonthlyStore reportStore,
                             String currentMonth, MonthlyStore currentStore) {}

    /**
     * Loads the current and previous month report data from the active storage implementation.
     *
     * @return the aggregated report data for the report month and current month
     * @throws Exception if the report data cannot be loaded
     */
    public static ReportData loadReportData() throws Exception {
        if (isCI()) {
            GitHubStore.ReportData d = GitHubStore.loadReportData();
            return new ReportData(d.reportMonth(), d.reportStore(),
                    d.currentMonth(), d.currentStore());
        }
        Calendar now = Calendar.getInstance();
        String current = String.format("%d-%02d",
                now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        Calendar prev = (Calendar) now.clone();
        prev.add(Calendar.MONTH, -1);
        String report = String.format("%d-%02d",
                prev.get(Calendar.YEAR), prev.get(Calendar.MONTH) + 1);

        return new ReportData(report, localLoad(report), current, localLoad(current));
    }

    /**
     * Loads the set of known test identifiers from the active storage implementation.
     *
     * @return the set of known test identifiers
     * @throws Exception if the identifiers cannot be loaded
     */
    public static Set<String> loadKnownTestIds() throws Exception {
        if (isCI()) return GitHubStore.loadKnownTestIds();
        Path file = STORE_DIR.resolve(KNOWN_IDS);
        if (!Files.exists(file)) return new HashSet<>();
        List<String> ids = MAPPER.readValue(file.toFile(), List.class);
        return new HashSet<>(ids);
    }

    /**
     * Saves the supplied known test identifiers using the active storage implementation.
     *
     * @param ids the test identifiers to persist
     * @throws Exception if the identifiers cannot be saved
     */
    public static void saveKnownTestIds(Set<String> ids) throws Exception {
        if (isCI()) { GitHubStore.saveKnownTestIds(ids); return; }
        ensureDir();
        MAPPER.writerWithDefaultPrettyPrinter()
                .writeValue(STORE_DIR.resolve(KNOWN_IDS).toFile(), new ArrayList<>(ids));
        LoggingUtil.info("[store] Saved " + ids.size() + " known test IDs locally");
    }

    /**
     * Loads the monthly store for the supplied month from the local filesystem.
     *
     * @param month the month key to load in {@code yyyy-MM} format
     * @return the loaded monthly store, or an empty store when no file exists
     * @throws Exception if the store file cannot be read
     */
    private static MonthlyStore localLoad(String month) throws Exception {
        Path file = STORE_DIR.resolve(month + ".json");
        if (!Files.exists(file)) return new MonthlyStore(month, new ArrayList<>());
        return MAPPER.readValue(file.toFile(), MonthlyStore.class);
    }

    /**
     * Writes the supplied monthly store to the local filesystem.
     *
     * @param store the monthly store to save
     * @throws Exception if the store cannot be written
     */
    private static void localSave(MonthlyStore store) throws Exception {
        ensureDir();
        MAPPER.writerWithDefaultPrettyPrinter()
                .writeValue(STORE_DIR.resolve(store.month() + ".json").toFile(), store);
    }

    /**
     * Deletes obsolete monthly store files while keeping the current month, previous month, and known test identifiers.
     *
     * @param currentMonth the current month key in {@code yyyy-MM} format
     * @throws Exception if listing the store directory fails
     */
    private static void pruneOldFiles(String currentMonth) throws Exception {
        String[] parts = currentMonth.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 2, 1);
        String prevMonth = String.format("%d-%02d",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);

        try (var stream = Files.list(STORE_DIR)) {
            stream.filter(p -> p.toString().endsWith(".json"))
                    .filter(p -> {
                        String m = p.getFileName().toString().replace(".json", "");
                        return !m.equals(currentMonth)
                                && !m.equals(prevMonth)
                                && !m.equals("known-test-ids");
                    })
                    .forEach(p -> {
                        try { Files.delete(p);
                            LoggingUtil.info("[store] Deleted old local store: " + p.getFileName());
                        } catch (IOException ignored) {}
                    });
        }
    }

    /**
     * Creates the local store directory when it does not already exist.
     *
     * @throws IOException if the directory cannot be created
     */
    private static void ensureDir() throws IOException {
        if (!Files.exists(STORE_DIR)) Files.createDirectories(STORE_DIR);
    }
}
