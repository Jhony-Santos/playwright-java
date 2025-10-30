package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import org.example.demoqa.pages.components.SideMenuComponent;

public class FormsPage extends BasePage {
    private final SideMenuComponent sideMenu;

    public FormsPage(Page page) {
        super(page);
        this.sideMenu = new SideMenuComponent(page);
    }

    public PracticeFormPage openPracticeForm() {
        Locator leftPanel = page.locator(".left-pannel");

        try {
            leftPanel.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(4000)); // curto: se não aparecer, cai no fallback
            removeObstructions();
            sideMenu.openItem("Practice Form");
            page.waitForURL("**/automation-practice-form");
        } catch (PlaywrightException ignored) {
            page.navigate("https://demoqa.com/automation-practice-form",
                    new Page.NavigateOptions()
                            .setWaitUntil(WaitUntilState.DOMCONTENTLOADED) // ✅ CORRETO
                            .setTimeout(20000)
            );
        }


        return new PracticeFormPage(page);
    }
}
