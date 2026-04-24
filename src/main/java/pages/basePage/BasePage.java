package pages.basePage;

import com.microsoft.playwright.Page;

public class BasePage {

    protected final Page page;
    protected final ActionContainer modulars;

    public BasePage(Page page) {
        this.page = page;
        this.modulars = new ActionContainer(page);
    }

}