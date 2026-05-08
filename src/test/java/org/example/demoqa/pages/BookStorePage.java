package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookStorePage extends BasePage {

    private static final Pattern URL = Pattern.compile(".*/books.*");

    public BookStorePage(Page page) {
        super(page);
    }

    public BookStorePage assertLoaded() {
        ensureAppIsUp(List.of("body", "#searchBox"), 60_000, true);
        assertThat(page).hasURL(URL);
        assertThat(searchBox()).isVisible();
        return this;
    }

    private Locator searchBox() {
        return page.locator("#searchBox");
    }

    private Locator bookLink(String title) {
        return page.locator("a", new Page.LocatorOptions().setHasText(title)).first();
    }

    public BookStorePage searchBook(String title) {
        assertLoaded();

        searchBox().fill("");
        searchBox().fill(title);

        assertThat(searchBox()).hasValue(title);
        assertThat(bookLink(title)).isVisible();

        return this;
    }

    public BookDetailsPage openBook(String title) {
        assertThat(bookLink(title)).isVisible();
        click(bookLink(title));
        return new BookDetailsPage(page);
    }
}