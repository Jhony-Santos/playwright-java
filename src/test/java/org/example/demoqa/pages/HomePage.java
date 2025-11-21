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


        page.navigate(
                BASE_URL,
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        // remove overlays / ads se o seu BasePage tiver isso
        removeObstructions();

        return this;
    }


    public ElementsPage openElements() {
        page.navigate(
                BASE_URL + "elements",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        removeObstructions();
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

    public InteractionsPage openInteractions() {
        openCard("Interactions", "interaction");
        return new InteractionsPage(page);
    }

    public BookStoreApplicationPage openBookStoreApplication() {
        openCard("Book Store Application", "books");
        return new BookStoreApplicationPage(page);
    }







}
