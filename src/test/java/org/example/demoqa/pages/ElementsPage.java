package org.example.demoqa.pages;

import com.microsoft.playwright.Page;

public class ElementsPage extends BasePage {
    public ElementsPage(Page page) { super(page); }

    public TextBoxPage openTextBox() {
        clickMenuByText("Text Box");
        page.waitForURL("**/text-box");
        return new TextBoxPage(page);
    }

    public CheckBoxPage openCheckBox() {
        clickMenuByText("Check Box");
        page.waitForURL("**/checkbox");
        return new CheckBoxPage(page);
    }

    public RadioButtonPage openRadioButton() {
        clickMenuByText("Radio Button");
        page.waitForURL("**/radio-button");
        return new RadioButtonPage(page);
    }
    public WebTablesPage openWebTables() {
        clickMenuByText("Web Tables");
        page.waitForURL("**/webtables");
        return new WebTablesPage(page);
    }


}
