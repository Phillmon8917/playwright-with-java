package modulars.browser;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import utils.logger.LoggingUtil;
import utils.timeout.TimeoutUtil;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BrowserActions {

    private final Page page;

    public BrowserActions(Page page) {
        this.page = page;
    }

    /** Navigates to the home page ("/"). */
    public void loadThePage(Page page) {
        page.navigate("/", new Page.NavigateOptions()
                .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
    }

    /** Reloads the page. */
    public void refresh(Page page, String methodName) {
        try {
            page.reload();
            LoggingUtil.info((methodName != null ? methodName : "refresh") + " - Page reloaded successfully");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "refresh")
                    + " - Failed to reload page: " + err.getMessage());
            throw err;
        }
    }

    /** Navigates to the given URL. */
    public void navigate(Page page, String url, String methodName) {
        try {
            page.navigate(url);
            LoggingUtil.info((methodName != null ? methodName : "navigate")
                    + " - Successfully navigated to " + url);
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "navigate")
                    + " - Failed to navigate to " + url + ": " + err.getMessage());
            throw err;
        }
    }

    /** Returns the current page URL. */
    public String getCurrentUrl(Page page, String methodName) {
        try {
            String url = page.url();
            LoggingUtil.info((methodName != null ? methodName : "getCurrentUrl")
                    + " - Current URL is " + url);
            return url;
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "getCurrentUrl")
                    + " - Failed to get current URL: " + err.getMessage());
            throw err;
        }
    }

    /** Returns the page title, polling until it becomes non-empty. */
    public String getTitle(Page page, String methodName) {
        try {
            page.waitForFunction("() => document.title.trim().length > 0");
            String title = page.title();
            LoggingUtil.info((methodName != null ? methodName : "getTitle")
                    + " - Page title is \"" + title + "\"");
            return title;
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "getTitle")
                    + " - Page title did not become non-empty: " + err.getMessage());
            throw err;
        }
    }

    /** Opens a new tab in the same browser context. */
    public Page newTab(String methodName) {
        try {
            Page newPage = page.context().newPage();
            LoggingUtil.info((methodName != null ? methodName : "newTab")
                    + " - Opened a new tab successfully");
            return newPage;
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "newTab")
                    + " - Failed to open new tab: " + err.getMessage());
            throw err;
        }
    }

    /** Closes the given page/tab. */
    public void closeTab(Page page, String methodName) {
        try {
            page.close();
            LoggingUtil.info((methodName != null ? methodName : "closeTab")
                    + " - Tab closed successfully");
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "closeTab")
                    + " - Failed to close tab: " + err.getMessage());
            throw err;
        }
    }

    /** Returns all open pages in the current browser context. */
    public java.util.List<Page> getAllTabs(String methodName) {
        try {
            java.util.List<Page> pages = page.context().pages();
            LoggingUtil.info((methodName != null ? methodName : "getAllTabs")
                    + " - Retrieved " + pages.size() + " tabs");
            return pages;
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "getAllTabs")
                    + " - Failed to get tabs: " + err.getMessage());
            throw err;
        }
    }

    /** Switches to the tab at the given index. */
    public Page switchToTab(int index, String methodName) {
        try {
            java.util.List<Page> pages = page.context().pages();
            if (index < 0 || index >= pages.size())
                throw new IndexOutOfBoundsException("Tab index " + index + " out of range");
            LoggingUtil.info((methodName != null ? methodName : "switchToTab")
                    + " - Switched to tab index " + index);
            return pages.get(index);
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "switchToTab")
                    + " - Failed to switch to tab index " + index + ": " + err.getMessage());
            throw err;
        }
    }

    /** Asserts the page URL matches the expected string or pattern. */
    public void assertUrl(Page page, String expected, String methodName) {
        try {
            assertThat(page).hasURL(expected);
            LoggingUtil.info((methodName != null ? methodName : "assertUrl")
                    + " - URL matches expected: " + expected);
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "assertUrl")
                    + " - URL did not match expected: " + expected + " - " + err.getMessage());
            throw err;
        }
    }

    /** Asserts the page title matches the expected string. */
    public void assertTitle(Page page, String expected, String methodName) {
        try {
            assertThat(page).hasTitle(expected);
            LoggingUtil.info((methodName != null ? methodName : "assertTitle")
                    + " - Page title matches expected: " + expected);
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "assertTitle")
                    + " - Page title did not match expected: " + expected + " - " + err.getMessage());
            throw err;
        }
    }

    /** Waits for the #page-loader element to become hidden. */
    public void waitForPageLoaderToDisappear(Page page, String methodName) {
        com.microsoft.playwright.Locator loader = page.locator("#page-loader div");
        try {
            if (loader.count() > 0) {
                loader.first().waitFor(new com.microsoft.playwright.Locator.WaitForOptions()
                        .setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN)
                        .setTimeout(TimeoutUtil.of(120000))
                );
                LoggingUtil.info(methodName + " - Loader disappeared (hidden or detached)");
            } else {
                LoggingUtil.info(methodName + " - Loader was not present, continuing");
            }
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Loader did not disappear: " + err.getMessage());
            throw err;
        }
    }

    /**
     * Waits for the page to reach the given load state.
     * state: "domcontentloaded", "load", or "networkidle"
     */
    public void waitForPageReady(Page page, String state, String methodName) {
        try {
            LoadState loadState = switch (state) {
                case "domcontentloaded" -> LoadState.DOMCONTENTLOADED;
                case "load"             -> LoadState.LOAD;
                default                 -> LoadState.NETWORKIDLE;
            };
            page.waitForLoadState(loadState);
            LoggingUtil.info((methodName != null ? methodName : "waitForPageReady")
                    + " - Page ready with state: " + state);
        } catch (Exception err) {
            LoggingUtil.error((methodName != null ? methodName : "waitForPageReady")
                    + " - Failed waiting for page ready (" + state + "): " + err.getMessage());
            throw err;
        }
    }

    /** Waits for a popup window to open on the given page. */
    public void waitForPopupEvent(Page page, String methodName) {
        try {
            page.waitForPopup(() -> {});
            LoggingUtil.info(methodName + " - Popup event detected");
        } catch (Exception err) {
            LoggingUtil.error(methodName + " - Popup event did not occur: " + err.getMessage());
            throw err;
        }
    }
}