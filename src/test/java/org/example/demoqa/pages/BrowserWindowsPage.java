package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BrowserWindowsPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/browser-windows/?(\\?.*)?$");

    public BrowserWindowsPage(Page page) {
        super(page);
    }

    private Locator newTabButton() { return page.locator("#tabButton"); }
    private Locator newWindowButton() { return page.locator("#windowButton"); }
    private Locator newWindowMessageButton() { return page.locator("#messageWindowButton"); }

    public BrowserWindowsPage assertPageLoaded() {
        safeRemoveObstructions();

        ensureAppIsUp(List.of("body", "#tabButton", "#windowButton", "#messageWindowButton"), 60_000, true);

        assertThat(page).hasURL(URL_REGEX);
        assertThat(newTabButton()).isVisible();
        assertThat(newWindowButton()).isVisible();
        assertThat(newWindowMessageButton()).isVisible();

        return this;
    }

    public Page openNewTab() {
        Page newPage = page.waitForPopup(() -> newTabButton().click());
        newPage.waitForLoadState();
        return newPage;
    }

    public Page openNewWindow() {
        Page newPage = page.waitForPopup(() -> newWindowButton().click());
        newPage.waitForLoadState();
        return newPage;
    }

    public Page openNewWindowMessage() {
        Page newPage = page.waitForPopup(() -> newWindowMessageButton().click());
        newPage.waitForLoadState();
        return newPage;
    }

    public void assertSampleHeading(Page openedPage) {
        Locator heading = openedPage.locator("#sampleHeading");
        heading.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        assertThat(heading).containsText("This is a sample page");
    }

    public void assertWindowMessageContent(Page openedPage) {
        openedPage.waitForLoadState();

        String bodyText;
        try {
            bodyText = openedPage.locator("body").innerText().trim();
        } catch (Exception e) {
            throw new AssertionError("Não foi possível ler o conteúdo da janela de mensagem.", e);
        }

        if (bodyText.isBlank()) {
            throw new AssertionError("A janela de mensagem abriu, mas o body veio vazio.");
        }
    }
}