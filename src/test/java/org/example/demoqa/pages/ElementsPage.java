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

    // NOVO: Buttons
    public ButtonsPage openButtons() {
        sideMenu.openItem("Buttons");
        page.waitForURL("**/buttons");
        return new ButtonsPage(page);
    }

    // NOVO: Links
    public LinksPage openLinks() {
        sideMenu.openItem("Links");
        page.waitForURL("**/links");
        return new LinksPage(page);
    }

    // Método genérico para qualquer item do menu lateral em Elements
    public BasePage openAccordionSection(String itemText, String expectedUrlPart) {
        sideMenu.openItem(itemText);
        page.waitForURL("**/" + expectedUrlPart);
        removeObstructions();
        return this;
    }

    // Alternativa: genérico só com clique (sem URL)
    public ElementsPage openAccordionSection(String itemText) {
        sideMenu.openItem(itemText);
        removeObstructions();
        return this;
    }





}
