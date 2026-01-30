package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.GetByRoleOptions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePage extends BasePage {

    private static final String BASE_URL = "https://demoqa.com/";
    private static final double TIMEOUT_MS = 30_000;

    public HomePage(Page page) { super(page); }



    public ElementsPage openElementsByClick() {
        removeObstructions();
        stepDelay();

        Locator elementsCard = page.locator("div.card")
                .filter(new Locator.FilterOptions().setHasText("Elements"))
                .first();

        elementsCard.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(30_000));
        assertThat(elementsCard).isVisible();

        elementsCard.scrollIntoViewIfNeeded();
        stepDelay();

        try {
            elementsCard.click(new Locator.ClickOptions().setTimeout(30_000));
        } catch (Exception e) {
            elementsCard.click(new Locator.ClickOptions().setForce(true).setTimeout(30_000));
        }

        page.waitForURL("**/elements", new Page.WaitForURLOptions().setTimeout(30_000));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        Locator appRoot = page.locator("#app");
        appRoot.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(30_000));

        // 2) Diagnóstico: o que existe no DOM?
        int headerCount = page.locator(".main-header, header, h1, h2").count();
        int leftCount = page.locator(".left-pannel, .left-panel").count();
        int menuCount = page.locator("ul.menu-list").count();


        if (leftCount == 0 && menuCount == 0) {
            System.out.println("Side menu não montou após clique. Fazendo fallback para navegação direta...");
            page.navigate(BASE_URL + "elements",
                    new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
            removeObstructions();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        }


        // 3) Se ainda assim não montou nada, imprime um pedaço do HTML e do texto do body
        if (headerCount == 0 && leftCount == 0) {
            String html = page.content();
            System.out.println("HTML snippet:\n" + (html.length() > 800 ? html.substring(0, 800) : html));

            String bodyText = page.locator("body").innerText();
            System.out.println("BODY text snippet:\n" + (bodyText.length() > 400 ? bodyText.substring(0, 400) : bodyText));
        }

        removeObstructions();

        page.onConsoleMessage(msg ->
                System.out.println("[console][" + msg.type() + "] " + msg.text())
        );

        page.onPageError(err ->
                System.out.println("[pageerror] " + err)
        );

        stepDelay();

        return new ElementsPage(page);
    }





    public HomePage gotoHome() {
        page.navigate(
                BASE_URL,
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

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
        removeObstructions();

        Locator heading = page.getByRole(
                AriaRole.HEADING,
                new GetByRoleOptions().setName(headingText)
        );

        heading.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(TIMEOUT_MS));

        heading.scrollIntoViewIfNeeded();
        removeObstructions();

        heading.click(new Locator.ClickOptions().setTimeout(TIMEOUT_MS));

        page.waitForURL("**/" + urlSuffix, new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        removeObstructions();
    }

    public FormsPage openForms() {
        openCard("Forms", "forms");
        return new FormsPage(page);
    }

    public AlertsFrameWindowsPage openAlertsFrameWindows() {
        openCard("Alerts, Frame & Windows", "alertsWindows");
        return new AlertsFrameWindowsPage(page);
    }

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
