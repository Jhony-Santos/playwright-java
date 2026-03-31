package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MenuPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/menu/?(\\?.*)?$");

    private final Locator mainItem1;
    private final Locator mainItem2;
    private final Locator mainItem3;

    private final Locator mainItem2Container;
    private final Locator mainItem2SubMenu;

    private final Locator subItem1;
    private final Locator subItem2;
    private final Locator subSubList;

    private final Locator subSubListContainer;
    private final Locator subSubMenu;
    private final Locator subSubItem1;
    private final Locator subSubItem2;

    public MenuPage(Page page) {
        super(page);

        this.mainItem1 = page.locator("#nav > li > a:text-is('Main Item 1')");
        this.mainItem2 = page.locator("#nav > li > a:text-is('Main Item 2')");
        this.mainItem3 = page.locator("#nav > li > a:text-is('Main Item 3')");

        this.mainItem2Container = page.locator("#nav > li")
                .filter(new Locator.FilterOptions().setHas(page.locator("a:text-is('Main Item 2')")))
                .first();

        this.mainItem2SubMenu = mainItem2Container.locator("ul").first();

        this.subItem1 = mainItem2SubMenu.locator("a:text-is('Sub Item')").nth(0);
        this.subItem2 = mainItem2SubMenu.locator("a:text-is('Sub Item')").nth(1);
        this.subSubList = mainItem2SubMenu.locator("a:text-is('SUB SUB LIST »')").first();

        this.subSubListContainer = mainItem2SubMenu.locator("li")
                .filter(new Locator.FilterOptions().setHas(page.locator("a:text-is('SUB SUB LIST »')")))
                .first();

        this.subSubMenu = subSubListContainer.locator("ul").first();
        this.subSubItem1 = subSubMenu.locator("a:text-is('Sub Sub Item 1')").first();
        this.subSubItem2 = subSubMenu.locator("a:text-is('Sub Sub Item 2')").first();
    }

    public MenuPage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(mainItem1).isVisible();
        assertThat(mainItem2).isVisible();
        assertThat(mainItem3).isVisible();
        return this;
    }

    public MenuPage hoverMainItem2() {
        removeObstructionsSafe();
        moveMouseToCenter(mainItem2);
        page.waitForTimeout(400);
        return this;
    }

    public MenuPage hoverSubSubList() {
        removeObstructionsSafe();
        moveMouseToCenter(subSubList);
        page.waitForTimeout(400);
        return this;
    }

    public MenuPage assertSubMenuVisible() {
        waitUntilDisplayed(mainItem2SubMenu);
        assertThat(subItem1).isVisible();
        assertThat(subItem2).isVisible();
        assertThat(subSubList).isVisible();
        return this;
    }

    public MenuPage assertSubSubMenuVisible() {
        waitUntilDisplayed(subSubMenu);
        assertThat(subSubItem1).isVisible();
        assertThat(subSubItem2).isVisible();
        return this;
    }

    private void moveMouseToCenter(Locator locator) {
        locator.scrollIntoViewIfNeeded();

        BoundingBox box = locator.boundingBox();
        if (box == null) {
            throw new RuntimeException("Nao foi possivel obter bounding box do elemento.");
        }

        double x = box.x + (box.width / 2.0);
        double y = box.y + (box.height / 2.0);

        page.mouse().move(x, y);
    }

    private void waitUntilDisplayed(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(5000));
    }
}