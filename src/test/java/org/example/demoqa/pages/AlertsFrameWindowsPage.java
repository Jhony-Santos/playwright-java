package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import org.example.demoqa.pages.components.SideMenuComponent;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AlertsFrameWindowsPage extends BasePage {

    private static final String BASE_URL = "https://demoqa.com/";
    private static final Pattern URL_REGEX = Pattern.compile(".*/(alertsWindows|browser-windows|alerts|frames|nestedframes|modal-dialogs)/?(\\?.*)?$");

    private final SideMenuComponent sideMenu;

    public AlertsFrameWindowsPage(Page page) {
        super(page);
        this.sideMenu = new SideMenuComponent(page);
    }

    public AlertsFrameWindowsPage assertPageLoaded() {
        safeRemoveObstructions();

        ensureAppIsUp(List.of("body"), 60_000, true);

        assertThat(page).hasURL(URL_REGEX);

        try {
            Locator header = page.locator("div.main-header, h1").first();
            header.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5_000));
            assertThat(header).containsText("Alerts, Frame & Windows");
        } catch (Exception ignored) {
        }

        return this;
    }

    public AlertsFrameWindowsPage gotoAlertsFrameWindows() {
        page.navigate(BASE_URL + "alertsWindows",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/alertsWindows", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);

        return this;
    }

    public BrowserWindowsPage openBrowserWindows() {
        page.navigate(BASE_URL + "browser-windows",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/browser-windows", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#tabButton"), 60_000, true);

        return new BrowserWindowsPage(page);
    }

    public AlertsPage openAlerts() {
        page.navigate(BASE_URL + "alerts",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/alerts", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#alertButton"), 60_000, true);

        return new AlertsPage(page);
    }

    public FramesPage openFrames() {
        page.navigate(BASE_URL + "frames",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/frames", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#frame1"), 60_000, true);

        return new FramesPage(page);
    }

    public NestedFramesPage openNestedFrames() {
        page.navigate(BASE_URL + "nestedframes",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/nestedframes", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#frame1"), 60_000, true);

        return new NestedFramesPage(page);
    }


    public ModalDialogsPage openModalDialogs() {
        page.navigate(BASE_URL + "modal-dialogs",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/modal-dialogs", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#showSmallModal"), 60_000, true);

        return new ModalDialogsPage(page);
    }

    public AlertsFrameWindowsPage openItemViaSideMenu(String itemText) {
        safeRemoveObstructions();
        sideMenu.openItem(itemText);
        return this;
    }
}