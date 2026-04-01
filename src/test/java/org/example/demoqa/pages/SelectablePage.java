package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SelectablePage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/selectable/?(\\?.*)?$");
    private static final Pattern ACTIVE_CLASS_REGEX = Pattern.compile(".*\\bactive\\b.*");

    private final Locator listTab;
    private final Locator gridTab;

    private final Locator listContainer;
    private final Locator gridContainer;

    public SelectablePage(Page page) {
        super(page);

        this.listTab = page.locator("#demo-tab-list");
        this.gridTab = page.locator("#demo-tab-grid");

        this.listContainer = page.locator("#verticalListContainer");
        this.gridContainer = page.locator("#gridContainer");
    }

    public SelectablePage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(listTab).isVisible();
        assertThat(gridTab).isVisible();
        return this;
    }

    public SelectablePage openListTab() {
        click(listTab);
        assertThat(listTab).hasAttribute("aria-selected", "true");
        assertThat(listContainer).isVisible();
        return this;
    }

    public SelectablePage openGridTab() {
        click(gridTab);
        assertThat(gridTab).hasAttribute("aria-selected", "true");
        assertThat(gridContainer).isVisible();
        return this;
    }

    public SelectablePage selectListItem(String text) {
        openListTab();
        Locator item = listContainer.locator("li")
                .filter(new Locator.FilterOptions().setHasText(text))
                .first();

        click(item);
        return this;
    }

    public SelectablePage selectGridItem(String text) {
        openGridTab();
        Locator item = gridContainer.locator("li")
                .filter(new Locator.FilterOptions().setHasText(text))
                .first();

        click(item);
        return this;
    }

    public SelectablePage assertListItemSelected(String text) {
        Locator selectedItem = listContainer.locator("li")
                .filter(new Locator.FilterOptions().setHasText(text))
                .first();

        assertThat(selectedItem).hasClass(ACTIVE_CLASS_REGEX);
        return this;
    }

    public SelectablePage assertGridItemSelected(String text) {
        Locator selectedItem = gridContainer.locator("li")
                .filter(new Locator.FilterOptions().setHasText(text))
                .first();

        assertThat(selectedItem).hasClass(ACTIVE_CLASS_REGEX);
        return this;
    }
}