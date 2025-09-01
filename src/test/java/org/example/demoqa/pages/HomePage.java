package org.example.demoqa.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import static com.microsoft.playwright.options.AriaRole.HEADING;

public class HomePage extends BasePage {
    public HomePage(Page page) { super(page); }

    public HomePage gotoHome() {
        page.navigate("https://demoqa.com/");
        return this;
    }

    public ElementsPage openElements() {
        clickHeading("Elements");
        page.waitForURL("**/elements");
        return new ElementsPage(page);
    }

    private void openCard(String headingText, String urlSuffix) {
        page.getByRole(HEADING, new GetByRoleOptions().setName(headingText)).click();
        page.waitForURL("**/" + urlSuffix);
    }

    public FormsPage openForms() {
        openCard("Forms", "forms");
        return new FormsPage(page);
    }

    /** Abre a seção Alerts, Frame & Windows */
    public AlertsFrameWindowsPage openAlertsFrameWindows() {
        openCard("Alerts, Frame & Windows", "alertsWindows");
        return new AlertsFrameWindowsPage(page);
    }

    /** Abre a seção Widgets */
    public WidgetsPage openWidgets() {
        openCard("Widgets", "widgets");
        return new WidgetsPage(page);
    }



}
