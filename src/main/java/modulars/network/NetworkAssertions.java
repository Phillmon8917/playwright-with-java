package modulars.network;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import utils.logger.LoggingUtil;
import java.util.function.Predicate;

public class NetworkAssertions {

    private final Page page;

    public NetworkAssertions(Page page) {
        this.page = page;
    }

    /**
     * Asserts that a network response matching the given URL/regex (and optional HTTP method)
     * returns the expected HTTP status code.
     *
     * @param urlOrRegex     Exact URL string or regex pattern to match.
     * @param expectedStatus Expected HTTP status code.
     * @param trigger        Optional action that triggers the request (set up before firing).
     * @param httpMethod     Optional HTTP method filter: GET, POST, PUT, DELETE, PATCH.
     * @param requestName    Display name for logging.
     * @param methodName     Calling method name for logging.
     */
    public void assertNetworkRequest(
            Object urlOrRegex,
            int expectedStatus,
            Runnable trigger,
            String httpMethod,
            String requestName,
            String methodName) {

        Predicate<Response> matcher = resp -> {
            boolean urlMatch;
            if (urlOrRegex instanceof String url) {
                urlMatch = resp.url().equals(url);
            } else if (urlOrRegex instanceof java.util.regex.Pattern pattern) {
                urlMatch = pattern.matcher(resp.url()).find();
            } else {
                urlMatch = false;
            }
            boolean methodMatch = httpMethod == null
                    || resp.request().method().equalsIgnoreCase(httpMethod);
            return urlMatch && methodMatch;
        };

        Response response = page.waitForResponse(matcher, () -> {
            if (trigger != null) trigger.run();
        });

        org.junit.jupiter.api.Assertions.assertEquals(expectedStatus, response.status(),
                (requestName != null ? requestName : "Request") + " status mismatch");

        LoggingUtil.info((methodName != null ? methodName : "NetworkAssertion")
                + " - " + (requestName != null ? requestName : "Request")
                + " matched with status " + expectedStatus
                + (httpMethod != null ? " and method " + httpMethod : ""));
    }

    /** Overload without trigger, httpMethod, requestName, or methodName. */
    public void assertNetworkRequest(Object urlOrRegex, int expectedStatus) {
        assertNetworkRequest(urlOrRegex, expectedStatus, null, null, null, null);
    }
}