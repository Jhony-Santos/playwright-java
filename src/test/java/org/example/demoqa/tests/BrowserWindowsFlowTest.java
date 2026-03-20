package org.example.demoqa.tests;

import com.microsoft.playwright.Page;
import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.BrowserWindowsPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class BrowserWindowsFlowTest extends BaseTest {

    @Test
    void shouldOpenNewTabAndValidateSampleHeading() {
        BrowserWindowsPage browserWindows = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openBrowserWindows();

        browserWindows.assertPageLoaded();

        Page newTab = browserWindows.openNewTab();
        browserWindows.assertSampleHeading(newTab);

        newTab.close();
    }

    @Test
    void shouldOpenNewWindowAndValidateSampleHeading() {
        BrowserWindowsPage browserWindows = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openBrowserWindows();

        browserWindows.assertPageLoaded();

        Page newWindow = browserWindows.openNewWindow();
        browserWindows.assertSampleHeading(newWindow);

        newWindow.close();
    }

    @Test
    void shouldOpenNewWindowMessage() {
        BrowserWindowsPage browserWindows = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openBrowserWindows();

        browserWindows.assertPageLoaded();

        Page messageWindow = browserWindows.openNewWindowMessage();
        browserWindows.assertWindowMessageContent(messageWindow);

        messageWindow.close();
    }
}