package report;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import utils.logger.LoggingUtil;

public class BuildReport {

    /**
     * Builds the static HTML report by injecting serialized run data into the report template.
     *
     * @param args command-line arguments passed to the application
     * @throws Exception if the report data cannot be loaded or the output file cannot be written
     */
    public static void main(String[] args) throws Exception {
        Store.ReportData data = Store.loadReportData();

        LoggingUtil.info("[build-report] Month : " + data.reportMonth());
        LoggingUtil.info("[build-report] Runs  : " + data.reportStore().runs().size());

        Path templatePath = Path.of("src/main/java/report/report.html");
        Path outPath      = Path.of("src/dist/report.html");

        if (!Files.exists(outPath.getParent()))
            Files.createDirectories(outPath.getParent());

        String html = Files.readString(templatePath);

        if (!html.contains("</head>"))
            throw new RuntimeException("[build-report] Template missing </head> — cannot inject data");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("month", data.reportMonth());
        payload.put("runs",  data.reportStore().runs());

        String injected = "<script>window.__REPORT_DATA__ = "
                + mapper.writeValueAsString(payload) + ";</script>";

        html = html.replace("</head>", injected + "\n</head>");
        Files.writeString(outPath, html);

        LoggingUtil.info("[build-report] Report written → " + outPath);
    }
}
