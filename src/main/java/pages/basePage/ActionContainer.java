package pages.basePage;

import com.microsoft.playwright.Page;
import modulars.assertions.ElementAssertions;
import modulars.browser.BrowserActions;
import modulars.calendar.CalendarNavigator;
import modulars.dropdowns.DropdownActions;
import modulars.elements.ElementActions;
import modulars.iframe.IframeActions;
import modulars.keyboard.KeyboardActions;
import modulars.network.NetworkAssertions;
import modulars.toggle.ToggleActions;

public class ActionContainer {

    public final ElementAssertions assertions;
    public final BrowserActions browser;
    public final CalendarNavigator calendar;
    public final DropdownActions dropdown;
    public final ElementActions elements;
    public final IframeActions iframe;
    public final KeyboardActions keyboard;
    public final NetworkAssertions network;
    public final ToggleActions toggle;

    public ActionContainer(Page page) {
        this.assertions = new ElementAssertions();
        this.dropdown = new DropdownActions();
        this.elements = new ElementActions();
        this.iframe = new IframeActions();
        this.toggle = new ToggleActions();
        this.browser = new BrowserActions(page);
        this.calendar = new CalendarNavigator(page);
        this.keyboard = new KeyboardActions(page);
        this.network = new NetworkAssertions(page);
    }
}