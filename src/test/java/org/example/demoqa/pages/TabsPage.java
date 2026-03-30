package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TabsPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/tabs/?(\\?.*)?$");

    private final Locator whatTab;
    private final Locator originTab;
    private final Locator useTab;
    private final Locator moreTab;

    private final Locator whatTabPane;
    private final Locator originTabPane;
    private final Locator useTabPane;

    public TabsPage(Page page) {
        super(page);

        this.whatTab = page.locator("#demo-tab-what");
        this.originTab = page.locator("#demo-tab-origin");
        this.useTab = page.locator("#demo-tab-use");
        this.moreTab = page.locator("#demo-tab-more");

        this.whatTabPane = page.locator("#demo-tabpane-what");
        this.originTabPane = page.locator("#demo-tabpane-origin");
        this.useTabPane = page.locator("#demo-tabpane-use");
    }

    public TabsPage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(whatTab).isVisible();
        assertThat(originTab).isVisible();
        assertThat(useTab).isVisible();
        assertThat(moreTab).isVisible();
        return this;
    }

    public TabsPage clickWhatTab() {
        click(whatTab);
        assertWhatTabActive();
        return this;
    }

    public TabsPage clickOriginTab() {
        click(originTab);
        assertOriginTabActive();
        return this;
    }

    public TabsPage clickUseTab() {
        click(useTab);
        assertUseTabActive();
        return this;
    }

    public TabsPage assertWhatTabActive() {
        assertThat(whatTab).hasAttribute("aria-selected", "true");
        assertThat(whatTabPane).isVisible();
        return this;
    }

    public TabsPage assertOriginTabActive() {
        assertThat(originTab).hasAttribute("aria-selected", "true");
        assertThat(originTabPane).isVisible();
        return this;
    }

    public TabsPage assertUseTabActive() {
        assertThat(useTab).hasAttribute("aria-selected", "true");
        assertThat(useTabPane).isVisible();
        return this;
    }

    public TabsPage assertMoreTabDisabled() {
        assertThat(moreTab).isDisabled();
        assertThat(moreTab).hasAttribute("aria-disabled", "true");
        return this;
    }

    public TabsPage assertWhatContent() {
        assertThat(whatTabPane).containsText("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        return this;
    }

    public TabsPage assertOriginContent() {
        assertThat(originTabPane).containsText("Contrary to popular belief, Lorem Ipsum is not simply random text.");
        assertThat(originTabPane).containsText("The standard chunk of Lorem Ipsum used since the 1500s");
        return this;
    }

    public TabsPage assertUseContent() {
        assertThat(useTabPane).containsText("It is a long established fact that a reader will be distracted");
        return this;
    }
}