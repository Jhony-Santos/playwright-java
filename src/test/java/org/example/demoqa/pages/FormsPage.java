package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import org.example.demoqa.pages.components.SideMenuComponent;

import java.util.List;

public class FormsPage extends BasePage {

    private static final double TIMEOUT_MS = 30_000;

    // Flag: -DopenPracticeFormViaMenu=true para forçar abrir via SideMenu
    private static final boolean OPEN_PRACTICE_FORM_VIA_MENU =
            Boolean.parseBoolean(System.getProperty("openPracticeFormViaMenu", "false"));

    private final SideMenuComponent sideMenu;

    public FormsPage(Page page) {
        super(page);
        this.sideMenu = new SideMenuComponent(page);
    }

    public PracticeFormPage openPracticeForm() {
        if (OPEN_PRACTICE_FORM_VIA_MENU) {
            return openPracticeFormViaMenu();
        }
        return openPracticeFormDirect();
    }

    // ------------------ Caminho mais estável ------------------

    private PracticeFormPage openPracticeFormDirect() {
        page.navigate("https://demoqa.com/automation-practice-form",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        page.waitForURL("**/automation-practice-form",
                new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));

        safeRemoveObstructions();

        // A Practice Form às vezes “monta” devagar; body + header costumam ser melhor do que só #app
        ensureAppIsUp(List.of("body", "div.main-header"), 60_000, true);

        // Confirma que o form realmente está pronto
        page.locator("#firstName").first()
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(TIMEOUT_MS));

        return new PracticeFormPage(page);
    }

    // ------------------ Fallback (se você quiser manter o fluxo real do menu) ------------------

    private PracticeFormPage openPracticeFormViaMenu() {
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);

        sideMenu.openItem("Practice Form");

        page.waitForURL("**/automation-practice-form",
                new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "div.main-header"), 60_000, true);

        page.locator("#firstName").first()
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(TIMEOUT_MS));

        return new PracticeFormPage(page);
    }
}