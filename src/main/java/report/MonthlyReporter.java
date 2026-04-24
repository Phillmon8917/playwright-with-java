package report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.TestAbortedException;

import report.types.RunResult;
import report.types.TestResultRecord;
import utils.logger.LoggingUtil;

public class MonthlyReporter implements
        BeforeAllCallback, AfterAllCallback,
        BeforeEachCallback, AfterEachCallback {

    private final List<TestResultRecord>  results        = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Long>       testStartTimes = Collections.synchronizedMap(new HashMap<>());
    private final Set<String>             currentTestIds = Collections.synchronizedSet(new HashSet<>());

    private long         startTime;
    private Set<String>  previousTestIds = new HashSet<>();

    /**
     * Initializes the reporter state before all tests in the current scope run.
     *
     * @param ctx the JUnit extension context for the current test container
     * @throws Exception if known test identifiers cannot be loaded
     */
    @Override
    public void beforeAll(ExtensionContext ctx) throws Exception {
        startTime       = System.currentTimeMillis();
        previousTestIds = Store.loadKnownTestIds();
        LoggingUtil.info("[reporter] Known test IDs loaded: " + previousTestIds.size());
    }

    /**
     * Records the start time and unique identifier for an individual test before execution.
     *
     * @param ctx the JUnit extension context for the test about to run
     */
    @Override
    public void beforeEach(ExtensionContext ctx) {
        String id = ctx.getUniqueId();
        currentTestIds.add(id);
        testStartTimes.put(id, System.currentTimeMillis());
    }

    /**
     * Captures the result details for a completed test and stores them for monthly reporting.
     *
     * @param ctx the JUnit extension context for the completed test
     */
    @Override
    public void afterEach(ExtensionContext ctx) {
        String id       = ctx.getUniqueId();
        long   duration = System.currentTimeMillis()
                - testStartTimes.getOrDefault(id, startTime);

        String status = "passed";
        String error  = null;

        if (ctx.getExecutionException().isPresent()) {
            Throwable ex = ctx.getExecutionException().get();
            status = (ex instanceof TestAbortedException) ? "skipped" : "failed";
            error  = ex.getMessage();
        }

        results.add(new TestResultRecord(
                id,
                ctx.getDisplayName(),
                ctx.getParent().map(ExtensionContext::getDisplayName).orElse("")
                        + " > " + ctx.getDisplayName(),
                ctx.getTestClass().map(c -> c.getSimpleName() + ".java").orElse("unknown"),
                ctx.getParent().map(ExtensionContext::getDisplayName).orElse("root"),
                status,
                duration,
                0,
                error,
                List.of(),
                !previousTestIds.contains(id)
        ));
    }

    /**
     * Aggregates the collected test results, persists the run summary, and updates known test identifiers.
     *
     * @param ctx the JUnit extension context for the current test container
     * @throws Exception if the report data cannot be persisted
     */
    @Override
    public void afterAll(ExtensionContext ctx) throws Exception {
        long totalDuration = System.currentTimeMillis() - startTime;

        long passed   = results.stream().filter(r -> "passed".equals(r.status())).count();
        long failed   = results.stream().filter(r -> "failed".equals(r.status())).count();
        long skipped  = results.stream().filter(r -> "skipped".equals(r.status())).count();
        long timedOut = results.stream().filter(r -> "timedOut".equals(r.status())).count();
        long newTests = results.stream().filter(TestResultRecord::isNew).count();

        Calendar now   = Calendar.getInstance();
        String month   = String.format("%d-%02d", now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        String date    = String.format("%d-%02d-%02d", now.get(Calendar.YEAR),
                now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));

        String branch  = env("GITHUB_REF_NAME", gitCmd("git rev-parse --abbrev-ref HEAD"));
        String rawSha  = env("GITHUB_SHA",      gitCmd("git rev-parse HEAD"));
        String sha     = rawSha.length() > 7 ? rawSha.substring(0, 7) : rawSha;
        String runId   = env("GITHUB_RUN_ID",   UUID.randomUUID().toString());
        String environment = env("TEST_ENV", "main".equals(branch) ? "qa" : "develop");

        RunResult run = new RunResult(
                runId,
                now.toInstant().toString(),
                date, month, branch, sha, environment,
                results.size(),
                (int) passed, (int) failed, (int) skipped, (int) timedOut,
                totalDuration,
                (int) newTests,
                new ArrayList<>(results)
        );

        Store.appendRun(run);

        Set<String> merged = new HashSet<>(previousTestIds);
        merged.addAll(currentTestIds);
        Store.saveKnownTestIds(merged);

        LoggingUtil.info("[reporter] Run " + runId + " | Month: " + month
                + " | Passed: " + passed + " | Failed: " + failed
                + " | Skipped: " + skipped + " | New: " + newTests);
    }

    /**
     * Returns the environment variable value for the supplied key or a fallback when it is blank.
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
     * Executes a git command and returns its trimmed standard output.
     *
     * @param cmd the git command to execute
     * @return the command output, or {@code "unknown"} when execution fails
     */
    private static String gitCmd(String cmd) {
        try {
            return new String(Runtime.getRuntime().exec(cmd)
                    .getInputStream().readAllBytes()).trim();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
