package org.example.demoqa.pages;

import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AlertsPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/alerts/?(\\?.*)?$");

    public AlertsPage(Page page) {
        super(page);
    }

    private Locator simpleAlertButton() { return page.locator("#alertButton"); }
    private Locator timerAlertButton() { return page.locator("#timerAlertButton"); }
    private Locator confirmButton() { return page.locator("#confirmButton"); }
    private Locator promptButton() { return page.locator("#promtButton"); }

    private Locator confirmResult() { return page.locator("#confirmResult"); }
    private Locator promptResult() { return page.locator("#promptResult"); }

    public AlertsPage assertPageLoaded() {
        safeRemoveObstructions();

        ensureAppIsUp(
                List.of("body", "#alertButton", "#timerAlertButton", "#confirmButton", "#promtButton"),
                60_000,
                true
        );

        assertThat(page).hasURL(URL_REGEX);
        assertThat(simpleAlertButton()).isVisible();
        assertThat(timerAlertButton()).isVisible();
        assertThat(confirmButton()).isVisible();
        assertThat(promptButton()).isVisible();

        try {
            Locator header = page.locator("div.main-header, h1").first();
            header.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5_000));
            assertThat(header).containsText("Alerts");
        } catch (Exception ignored) {
        }

        return this;
    }

    public AlertsPage openSimpleAlertAndAccept() {
        page.onceDialog(Dialog::accept);
        simpleAlertButton().click();
        return this;
    }

    public AlertsPage openTimerAlertAndAccept() {
        page.onceDialog(Dialog::accept);
        timerAlertButton().click();
        return this;
    }

    public AlertsPage openConfirmAndAccept() {
        page.onceDialog(Dialog::accept);
        confirmButton().click();
        return this;
    }

    public AlertsPage openConfirmAndDismiss() {
        page.onceDialog(Dialog::dismiss);
        confirmButton().click();
        return this;
    }

    public AlertsPage openPromptAndAccept(String text) {
        page.onceDialog(dialog -> dialog.accept(text));
        promptButton().click();
        return this;
    }

    public AlertsPage openPromptAndDismiss() {
        page.onceDialog(Dialog::dismiss);
        promptButton().click();
        return this;
    }

    public AlertsPage assertConfirmAccepted() {
        assertThat(confirmResult()).containsText("You selected Ok");
        return this;
    }

    public AlertsPage assertConfirmDismissed() {
        assertThat(confirmResult()).containsText("You selected Cancel");
        return this;
    }

    public AlertsPage assertPromptResult(String expectedText) {
        assertThat(promptResult()).containsText(expectedText);
        return this;
    }
}