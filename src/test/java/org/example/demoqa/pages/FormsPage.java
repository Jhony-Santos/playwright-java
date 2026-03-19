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

        // ✅ readiness baseado no que importa: o formulário
        ensureAppIsUp(List.of("body", "#firstName"), 60_000, true);

        // Header é só best-effort; não bloqueia o fluxo
        try {
            page.locator("div.main-header, h1").first()
                    .waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(5_000));
        } catch (Exception ignored) {}

        // Confirma que o form realmente está pronto
        page.locator("#firstName").first()
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(TIMEOUT_MS));

        page.locator("#submit").first()
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(TIMEOUT_MS));

        return new PracticeFormPage(page);
    }

    // ------------------ Fallback (fluxo real pelo menu) ------------------

    private PracticeFormPage openPracticeFormViaMenu() {
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);

        sideMenu.openItem("Practice Form");

        page.waitForURL("**/automation-practice-form",
                new Page.WaitForURLOptions().setTimeout(TIMEOUT_MS));

        safeRemoveObstructions();

        // ✅ readiness baseado no form
        ensureAppIsUp(List.of("body", "#firstName"), 60_000, true);

        try {
            page.locator("div.main-header, h1").first()
                    .waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(5_000));
        } catch (Exception ignored) {}

        page.locator("#firstName").first()
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(TIMEOUT_MS));

        page.locator("#submit").first()
                .waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(TIMEOUT_MS));

        return new PracticeFormPage(page);
    }
}