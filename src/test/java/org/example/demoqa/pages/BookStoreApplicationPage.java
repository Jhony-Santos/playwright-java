package org.example.demoqa.pages;

import com.microsoft.playwright.Page;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookStoreApplicationPage extends BasePage {

    private static final Pattern URL = Pattern.compile(".*/books.*");

    public BookStoreApplicationPage(Page page) {
        super(page);
    }

    public BookStoreApplicationPage assertLoaded() {
        ensureAppIsUp(List.of("body", "#searchBox"), 60_000, true);
        assertThat(page).hasURL(URL);
        assertThat(page.locator("#searchBox")).isVisible();
        return this;
    }

    public LoginPage openLogin() {
        click(page.locator("span.text:has-text('Login')").first());
        return new LoginPage(page);
    }

    public BookStorePage openBookStore() {
        click(page.locator("span.text:has-text('Book Store')").first());
        return new BookStorePage(page);
    }

    public ProfilePage openProfile() {
        click(page.locator("span.text:has-text('Profile')").first());
        return new ProfilePage(page);
    }
}