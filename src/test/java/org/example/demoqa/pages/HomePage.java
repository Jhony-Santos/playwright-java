package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

import java.util.List;

public class HomePage extends BasePage {

    private static final String BASE_URL = "https://demoqa.com/";
    private static final double TIMEOUT_MS = 30_000;

    // Flag: -DopenElementsDirect=true para usar navegação direta
    private static final boolean OPEN_ELEMENTS_DIRECT =
            Boolean.parseBoolean(System.getProperty("openElementsDirect", "false"));

    public HomePage(Page page) {
        super(page);
    }

    public HomePage gotoHome() {
        page.navigate(BASE_URL, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        afterNavigation();
        return this;
    }

    public ElementsPage openElements() {
        if (OPEN_ELEMENTS_DIRECT) {
            page.navigate(BASE_URL + "elements", new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
            afterNavigation();
            return new ElementsPage(page);
        }
        return openElementsByClick();
    }

    public ElementsPage openElementsByClick() {
        gotoHome();

        Locator elementsCard = page.locator("div.card")
                .filter(new Locator.FilterOptions().setHasText("Elements"))
                .first();

        elementsCard.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(TIMEOUT_MS));

        elementsCard.scrollIntoViewIfNeeded();

        try {
            elementsCard.click(new Locator.ClickOptions().setTimeout(TIMEOUT_MS));
        } catch (Exception e) {
            elementsCard.click(new Locator.ClickOptions().setForce(true).setTimeout(TIMEOUT_MS));
        }

        page.waitForURL("**/elements", new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        afterNavigation();
        return new ElementsPage(page);
    }

    // ✅ Seus testes chamam isso
    public FormsPage openForms() {
        openCardByHeading("Forms", "forms");
        return new FormsPage(page);
    }

    public AlertsFrameWindowsPage openAlertsFrameWindows() {
        openCardByHeading("Alerts, Frame & Windows", "alertsWindows");
        return new AlertsFrameWindowsPage(page);
    }

    public WidgetsPage openWidgets() {
        openCardByHeading("Widgets", "widgets");
        return new WidgetsPage(page);
    }

    public InteractionsPage openInteractions() {
        openCardByHeading("Interactions", "interaction");
        return new InteractionsPage(page);
    }

    public BookStoreApplicationPage openBookStoreApplication() {
        openCardByHeading("Book Store Application", "books");
        return new BookStoreApplicationPage(page);
    }

    private void afterNavigation() {
        safeRemoveObstructions();
        // ✅ Home às vezes não “monta” #app rápido; então âncora mínima
        ensureAppIsUp(List.of("body"));
    }

    private void openCardByHeading(String headingText, String urlSuffix) {
        afterNavigation();

        Locator heading = page.getByRole(
                AriaRole.HEADING,
                new GetByRoleOptions().setName(headingText)
        );

        heading.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(TIMEOUT_MS));

        heading.scrollIntoViewIfNeeded();
        safeRemoveObstructions();

        heading.click(new Locator.ClickOptions().setTimeout(TIMEOUT_MS));

        page.waitForURL("**/" + urlSuffix, new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        afterNavigation();
    }
}