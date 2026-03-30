package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProgressBarPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/progress-bar/?(\\?.*)?$");

    private final Locator progressBar;
    private final Locator progressBarInner;
    private final Locator startStopButton;
    private final Locator resetButton;

    public ProgressBarPage(Page page) {
        super(page);
        this.progressBar = page.locator("#progressBar");
        this.progressBarInner = page.locator("#progressBar [role='progressbar']");
        this.startStopButton = page.locator("#startStopButton");
        this.resetButton = page.locator("#resetButton");
    }

    public ProgressBarPage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(progressBar).isVisible();
        assertThat(startStopButton).isVisible();
        return this;
    }

    public ProgressBarPage assertInitialState() {
        assertProgressValue(0);
        assertThat(startStopButton).hasText("Start");
        return this;
    }

    public ProgressBarPage start() {
        assertThat(startStopButton).hasText("Start");
        click(startStopButton);
        assertThat(startStopButton).hasText("Stop");
        return this;
    }

    public ProgressBarPage stop() {
        assertThat(startStopButton).hasText("Stop");
        click(startStopButton);
        assertThat(startStopButton).hasText("Start");
        return this;
    }

    public ProgressBarPage reset() {
        assertThat(resetButton).isVisible();
        click(resetButton);
        assertProgressValue(0);
        return this;
    }

    public ProgressBarPage waitUntilProgressAtLeast(int minValue) {
        page.waitForFunction(
                "(min) => {" +
                        "const el = document.querySelector('#progressBar [role=\"progressbar\"]');" +
                        "if (!el) return false;" +
                        "const value = Number(el.getAttribute('aria-valuenow') || '0');" +
                        "return value >= min;" +
                        "}",
                minValue,
                new Page.WaitForFunctionOptions().setTimeout(20_000)
        );
        return this;
    }

    public ProgressBarPage waitUntilComplete() {
        page.waitForFunction(
                "() => {" +
                        "const el = document.querySelector('#progressBar [role=\"progressbar\"]');" +
                        "if (!el) return false;" +
                        "const value = Number(el.getAttribute('aria-valuenow') || '0');" +
                        "return value === 100;" +
                        "}",
                null,
                new Page.WaitForFunctionOptions().setTimeout(30_000)
        );
        return this;
    }

    public ProgressBarPage assertProgressValue(int expected) {
        String expectedText = String.valueOf(expected);

        assertThat(progressBarInner).hasAttribute("aria-valuenow", expectedText);
        assertThat(progressBarInner).containsText(expected + "%");
        return this;
    }

    public int currentProgressValue() {
        String raw = progressBarInner.getAttribute("aria-valuenow");
        return raw == null ? 0 : Integer.parseInt(raw);
    }

    public ProgressBarPage assertProgressBetween(int minInclusive, int maxInclusive) {
        int actual = currentProgressValue();
        if (actual < minInclusive || actual > maxInclusive) {
            throw new AssertionError(
                    "Valor do progresso fora do intervalo esperado. Atual=" + actual +
                            ", esperado entre " + minInclusive + " e " + maxInclusive
            );
        }
        return this;
    }
}