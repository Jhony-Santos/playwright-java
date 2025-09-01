package org.example.demoqa.pages;

import com.microsoft.playwright.Page;

public class ElementsPage extends BasePage {
    public ElementsPage(Page page) { super(page); }

    public TextBoxPage openTextBox() {
        clickMenuByText("Text Box");
        page.waitForURL("**/text-box");
        return new TextBoxPage(page);
    }
}
