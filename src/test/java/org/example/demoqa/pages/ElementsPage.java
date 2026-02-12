package org.example.demoqa.pages;

import com.microsoft.playwright.Page;
import org.example.demoqa.pages.components.SideMenuComponent;
import com.microsoft.playwright.options.AriaRole;


public class ElementsPage extends BasePage {
    private final SideMenuComponent sideMenu;

    public ElementsPage(Page page) {
        super(page);
        this.sideMenu = new SideMenuComponent(page);
    }

    public BrokenLinksImagesPage openBrokenLinksImages() {
        page.waitForURL("**/elements");
        removeObstructions();
        stepDelay();

        sideMenu.openItem("Broken Links - Images");

        page.waitForURL("**/broken");
        removeObstructions();
        stepDelay();

        return new BrokenLinksImagesPage(page);
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

    public ButtonsPage openButtons() {
        sideMenu.openItem("Buttons");
        page.waitForURL("**/buttons");
        return new ButtonsPage(page);
    }

    public LinksPage openLinks() {
        sideMenu.openItem("Links");
        page.waitForURL("**/links");
        return new LinksPage(page);
    }

    public BasePage openAccordionSection(String itemText, String expectedUrlPart) {
        sideMenu.openItem(itemText);
        page.waitForURL("**/" + expectedUrlPart);
        removeObstructions();
        return this;
    }

    public ElementsPage openAccordionSection(String itemText) {
        sideMenu.openItem(itemText);
        removeObstructions();
        return this;
    }

    public UploadDownloadPage openUploadDownload() {
        sideMenu.openItem("Upload and Download");
        page.waitForURL("**/upload-download");
        removeObstructions();
        return new UploadDownloadPage(page);
    }


    public DynamicPropertiesPage openDynamicProperties() {
        page.waitForURL("**/elements");
        removeObstructions();
        stepDelay();

        sideMenu.openItem("Dynamic Properties");

        page.waitForURL("**/dynamic-properties");
        removeObstructions();
        stepDelay();

        return new DynamicPropertiesPage(page);
    }




}
