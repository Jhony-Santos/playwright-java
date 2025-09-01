package org.example.demoqa.pages;

import com.microsoft.playwright.Page;

public class HomePage extends BasePage {
    public HomePage(Page page) { super(page); }

    public HomePage gotoHome() {
        page.navigate("https://demoqa.com/");
        return this;
    }

    public ElementsPage openElements() {
        clickHeading("Elements");
        page.waitForURL("**/elements");
        return new ElementsPage(page);
    }
}
