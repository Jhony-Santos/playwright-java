package org.example.demoqa.pages;

import com.microsoft.playwright.Page;
import org.example.demoqa.pages.components.SideMenuComponent;

public class ElementsPage extends BasePage {
    private final SideMenuComponent sideMenu;

    public ElementsPage(Page page) {
        super(page);
        this.sideMenu = new SideMenuComponent(page);
    }

    public TextBoxPage openTextBox() {
        sideMenu.openItem("Text Box");
        page.waitForURL("**/text-box");
        return new TextBoxPage(page);
    }

    public CheckBoxPage openCheckBox() {
        sideMenu.openItem("Check Box");
        page.waitForURL("**/checkbox");
        return new CheckBoxPage(page);
    }

    public RadioButtonPage openRadioButton() {
        sideMenu.openItem("Radio Button");
        page.waitForURL("**/radio-button");
        return new RadioButtonPage(page);
    }

    public WebTablesPage openWebTables() {
        sideMenu.openItem("Web Tables");
        page.waitForURL("**/webtables");
        return new WebTablesPage(page);
    }
}
