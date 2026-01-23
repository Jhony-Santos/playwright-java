package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

public class ButtonsPage extends BasePage {

    private static final Pattern URL = Pattern.compile(".*/buttons$");

    public ButtonsPage(Page page) {
        super(page);
    }

    private Locator titleH1() {
        return page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName("Buttons")
                        .setLevel(1)
                        .setExact(true)
        );
    }

    public void assertPageLoaded() {
        assertLoaded(URL, titleH1());
    }

    // Locators
    private Locator doubleClickButton() {
        return page.locator("#doubleClickBtn");
    }

    private Locator rightClickButton() {
        return page.locator("#rightClickBtn");
    }

    private Locator dynamicClickButton() {
        return page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Click Me").setExact(true));
    }

    private Locator doubleClickMessage() {
        return page.locator("#doubleClickMessage");
    }

    private Locator rightClickMessage() {
        return page.locator("#rightClickMessage");
    }

    private Locator dynamicClickMessage() {
        return page.locator("#dynamicClickMessage");
    }

    // Ações
    public ButtonsPage doubleClick() {
        doubleClickButton().dblclick();
        return this;
    }

    public ButtonsPage rightClick() {
        rightClickButton().click(new Locator.ClickOptions().setButton(com.microsoft.playwright.options.MouseButton.RIGHT));
        return this;
    }

    public ButtonsPage clickDynamic() {
        dynamicClickButton().click();
        return this;
    }

    // Leitura/validação
    public Locator doubleClickResult() {
        return doubleClickMessage();
    }

    public Locator rightClickResult() {
        return rightClickMessage();
    }

    public Locator dynamicClickResult() {
        return dynamicClickMessage();
    }
}
