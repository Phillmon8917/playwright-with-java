package report;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import report.types.RunResult;
import utils.logger.LoggingUtil;

public class BuildReport {

    /**
     * Main method to build the test report. Loads report data, injects it into
     * an HTML template, writes the report to a file, and generates monthly
     * totals in JSON format.
     *
     * @param args command line arguments (not used)
     * @throws Exception if file operations or JSON processing fails
     */
    public static void main(String[] args) throws Exception {
        Store.ReportData data = Store.loadReportData();

        LoggingUtil.info("[build-report] Month : " + data.reportMonth());
        LoggingUtil.info("[build-report] Runs  : " + data.reportStore().runs().size());

        Path templatePath = Path.of("src/main/java/report/report.html");
        Path outPath = Path.of("src/dist/report.html");

        if (!Files.exists(outPath.getParent())) {
            Files.createDirectories(outPath.getParent());
        }

        String html = Files.readString(templatePath);

        if (!html.contains("</head>")) {
            throw new RuntimeException("[build-report] Template missing </head> — cannot inject data");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("month", data.reportMonth());
        payload.put("runs", data.reportStore().runs());

        String injected = "<script>window.__REPORT_DATA__ = "
                + mapper.writeValueAsString(payload) + ";</script>";

        html = html.replace("</head>", injected + "\n</head>");
        Files.writeString(outPath, html);

        LoggingUtil.info("[build-report] Report written → " + outPath);

        long totalTests = 0, passed = 0, failed = 0, skipped = 0, timedOut = 0, duration = 0;
        int newTests = 0;

        for (RunResult r : data.reportStore().runs()) {
            totalTests += r.totalTests();
            passed += r.passed();
            failed += r.failed();
            skipped += r.skipped();
            timedOut += r.timedOut();
            duration += r.duration();
            newTests += r.newTests();
        }

        long executed = totalTests - skipped;
        double passRate = executed > 0 ? (passed * 100.0 / executed) : 0.0;

        Map<String, Object> totals = new LinkedHashMap<>();
        totals.put("tests", totalTests);
        totals.put("passed", passed);
        totals.put("failures", failed);
        totals.put("errors", timedOut);
        totals.put("skipped", skipped);
        totals.put("passRate", String.format("%.1f", passRate));
        totals.put("runs", data.reportStore().runs().size());
        totals.put("duration", duration);
        totals.put("newTests", newTests);

        Path totalsPath = Path.of("src/dist/monthly-totals.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(totalsPath.toFile(), totals);

        LoggingUtil.info("[build-report] Totals written → " + totalsPath);
        LoggingUtil.info("[build-report] tests=" + totalTests + " passed=" + passed
                + " failed=" + failed + " skipped=" + skipped
                + " passRate=" + String.format("%.1f", passRate) + "%");
    }
}
