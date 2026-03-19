package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
            page.waitForURL("**/elements", new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));
            afterNavigation();
            return new ElementsPage(page);
        }
        return openElementsByClick();
    }

    public ElementsPage openElementsByClick() {
        gotoHome();

        // ✅ Card real (mais confiável que clicar no HEADING)
        Locator elementsCard = page.locator("div.card:has(h5:has-text('Elements'))").first();

        elementsCard.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(TIMEOUT_MS));

        elementsCard.scrollIntoViewIfNeeded();
        safeRemoveObstructions();

        try {
            elementsCard.click(new Locator.ClickOptions().setTimeout(TIMEOUT_MS));
        } catch (Exception e) {
            safeRemoveObstructions();
            elementsCard.click(new Locator.ClickOptions().setForce(true).setTimeout(TIMEOUT_MS));
        }

        page.waitForURL("**/elements", new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        afterNavigation();
        return new ElementsPage(page);
    }

    // ✅ Seus testes chamam isso
    public FormsPage openForms() {
        openCardByText("Forms", "forms");
        return new FormsPage(page);
    }

    public AlertsFrameWindowsPage openAlertsFrameWindows() {
        openCardByText("Alerts, Frame & Windows", "alertsWindows");
        return new AlertsFrameWindowsPage(page);
    }

    public WidgetsPage openWidgets() {
        openCardByText("Widgets", "widgets");
        return new WidgetsPage(page);
    }

    public InteractionsPage openInteractions() {
        openCardByText("Interactions", "interaction");
        return new InteractionsPage(page);
    }

    public BookStoreApplicationPage openBookStoreApplication() {
        openCardByText("Book Store Application", "books");
        return new BookStoreApplicationPage(page);
    }

    private void afterNavigation() {
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"));
    }

    private void openCardByText(String cardText, String urlSuffix) {
        afterNavigation();

        Locator card = page.locator("div.card:has-text('" + cardText + "')").first();
        card.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(TIMEOUT_MS));

        card.scrollIntoViewIfNeeded();
        safeRemoveObstructions();

        try {
            card.click(new Locator.ClickOptions().setTimeout(TIMEOUT_MS));
        } catch (Exception e) {
            safeRemoveObstructions();
            card.click(new Locator.ClickOptions().setForce(true).setTimeout(TIMEOUT_MS));
        }

        page.waitForURL("**/" + urlSuffix, new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        afterNavigation();
    }


}