package report.types;

import java.util.List;

public record TestResultRecord(
        String id,
        String title,
        String fullTitle,
        String file,
        String suite,
        String status,
        long duration,
        int retries,
        String error,
        List<String> tags,
        boolean isNew
) {}