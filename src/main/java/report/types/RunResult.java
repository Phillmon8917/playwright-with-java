package report.types;

import java.util.List;

public record RunResult(
        String runId,
        String timestamp,
        String date,
        String month,
        String branch,
        String commitSha,
        String environment,
        String runType,
        int totalTests,
        int passed,
        int failed,
        int skipped,
        int timedOut,
        long duration,
        int newTests,
        List<TestResultRecord> tests
        ) {

}
