package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ToolTipsPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/tool-tips/?(\\?.*)?$");

    private final Locator hoverButton;
    private final Locator hoverInput;
    private final Locator contraryLink;
    private final Locator tooltip;

    public ToolTipsPage(Page page) {
        super(page);
        this.hoverButton = page.locator("#toolTipButton");
        this.hoverInput = page.locator("#toolTipTextField");
        this.contraryLink = page.locator("#texToolTopContainer a").first();
        this.tooltip = page.locator(".tooltip-inner");
    }

    public ToolTipsPage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(hoverButton).isVisible();
        assertThat(hoverInput).isVisible();
        return this;
    }

    public ToolTipsPage hoverButton() {
        hoverButton.scrollIntoViewIfNeeded();
        hoverButton.hover();
        return this;
    }

    public ToolTipsPage hoverInput() {
        hoverInput.scrollIntoViewIfNeeded();
        hoverInput.hover();
        return this;
    }

    public ToolTipsPage hoverContraryLink() {
        contraryLink.scrollIntoViewIfNeeded();
        contraryLink.hover();
        return this;
    }

    public ToolTipsPage assertTooltip(String expectedText) {
        assertThat(tooltip).isVisible();
        assertThat(tooltip).hasText(expectedText);
        return this;
    }
}