package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProfilePage extends BasePage {

    public ProfilePage(Page page) {
        super(page);
    }

    public ProfilePage assertBookPresent(String book) {
        Locator row = page.locator("a:has-text('" + book + "')");
        assertThat(row).isVisible();
        return this;
    }
}