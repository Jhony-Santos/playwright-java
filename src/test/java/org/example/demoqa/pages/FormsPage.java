package org.example.demoqa.pages;

import com.microsoft.playwright.Page;
import org.example.demoqa.pages.components.SideMenuComponent;

public class FormsPage extends BasePage {
    private final SideMenuComponent sideMenu;

    public FormsPage(Page page) {
        super(page);
        this.sideMenu = new SideMenuComponent(page);
    }

    public PracticeFormPage openPracticeForm() {
        sideMenu.openItem("Practice Form");
        page.waitForURL("**/automation-practice-form");
        return new PracticeFormPage(page);
    }
}
