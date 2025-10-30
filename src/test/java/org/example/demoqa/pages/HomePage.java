package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;


public class HomePage extends BasePage {

    private static final String BASE_URL = "https://demoqa.com/";

    public HomePage(Page page) { super(page); }

    public HomePage gotoHome() {

        //page.navigate(BASE_URL);
        //page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        page.navigate(
                BASE_URL,
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        // 3) Âncora da página: o card "Elements"
        Locator elementsHeading = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Elements")
        );

        // 4) Garante que está visível antes de interagir
        elementsHeading.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        // 5) Evita click interceptado por overlays/ad placeholders

        removeObstructions();
        elementsHeading.scrollIntoViewIfNeeded();

        return this;
    }


    public ElementsPage openElements() {
        clickHeading("Elements");
        page.waitForURL("**/elements");
        return new ElementsPage(page);
    }

    private void openCard(String headingText, String urlSuffix) {
        page.getByRole(AriaRole.HEADING, new GetByRoleOptions().setName(headingText)).click();
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
