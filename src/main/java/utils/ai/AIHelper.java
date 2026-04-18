package utils.ai;

import utils.logger.LoggingUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIHelper
{
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String getAIAnswer(String question) {
        String prompt = "Answer this question directly, no explanation: " + question;

        try {
            String requestBody = String.format("""
                {
                  "prompt": "%s",
                  "max_tokens": 10
                }
                """, escapeJson(prompt));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.zerostep.ai/v1/completions"))
                    .header("Authorization", "Bearer " + System.getenv("ZERO_STEP_API_KEY"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return extractValue(response.body(), "\"text\":\"", "\"").trim();

        } catch (Exception e) {
            LoggingUtil.error("❌ ZeroStep failed: " + e.getMessage());
            return "AI answer unavailable";
        }
    }

    public static String analyzeFailure(String error, String html) {

        String prompt = """
                You are a Playwright testing expert.

                Analyze this failure and respond with:
                1. Why it failed
                2. A better locator if its locator issue or anything causing the issue
                3. How to fix it

                ERROR:
                """ + error + """

                HTML:
                """ + html.substring(0, Math.min(html.length(), 3000));

        try {
            String requestBody = String.format("""
                {
                  "contents": [{
                    "parts": [{ "text": "%s" }]
                  }],
                  "generationConfig": {
                    "maxOutputTokens": 1024
                  }
                }
                """, escapeJson(prompt));

            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
                    + System.getenv("GEMINI_API_KEY");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return extractValue(response.body(), "\"text\":\"", "\"").trim();

        } catch (Exception e) {
            LoggingUtil.error("❌ Gemini failed: " + e.getMessage());

            return """
                    AI analysis failed.

                    Reason: %s

                    Suggestion:
                    - Check locator
                    - Use Playwright trace viewer
                    - Verify element exists in DOM
                    """.formatted(error);
        }
    }

    private static String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private static String extractValue(String json, String start, String end) {
        int i = json.indexOf(start);
        if (i == -1) return "";

        int j = json.indexOf(end, i + start.length());
        if (j == -1) return "";

        return json.substring(i + start.length(), j);
    }
}
