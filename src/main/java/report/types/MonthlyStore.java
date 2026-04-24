package report.types;

import java.util.List;

public record MonthlyStore(
        String month,
        List<RunResult> runs
) {}