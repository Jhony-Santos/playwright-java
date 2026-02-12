package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DynamicPropertiesPage extends BasePage {

    private static final int TIMEOUT_MS = Integer.parseInt(System.getProperty("dynTimeout", "12000"));

    private final Locator enableAfterBtn;
    private final Locator colorChangeBtn;
    private final Locator visibleAfterBtn;

    public DynamicPropertiesPage(Page page) {
        super(page);
        this.enableAfterBtn = page.locator("#enableAfter");
        this.colorChangeBtn = page.locator("#colorChange");
        this.visibleAfterBtn = page.locator("#visibleAfter");
    }

    public void assertLoaded() {
        page.waitForURL("**/dynamic-properties");
        enableAfterBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(TIMEOUT_MS));
        assertThat(enableAfterBtn).isVisible();
    }

    // ---------------- Enable After ----------------
    public void assertEnableButtonDisabledInitially() {
        assertThat(enableAfterBtn).isDisabled();
    }

    public void waitEnableButtonEnabled() {
        // Espera até o atributo "disabled" virar false
        page.waitForFunction(
                "sel => { const el = document.querySelector(sel); return el && !el.disabled; }",
                "#enableAfter",
                new Page.WaitForFunctionOptions().setTimeout(TIMEOUT_MS)
        );
        assertThat(enableAfterBtn).isEnabled();
    }

    // ---------------- Color Change ----------------
    public String colorButtonClass() {
        return colorChangeBtn.getAttribute("class");
    }

    public void waitColorButtonClassToChange(String beforeClass) {
        // Espera o className mudar (determinístico; não depende de CSS cascade)
        page.waitForFunction(
                "args => {" +
                        "const [sel, before] = args;" +
                        "const el = document.querySelector(sel);" +
                        "return el && el.className !== before;" +
                        "}",
                new Object[]{"#colorChange", beforeClass},
                new Page.WaitForFunctionOptions().setTimeout(TIMEOUT_MS)
        );
    }

    // Alternativa ainda mais forte: esperar aparecer "text-success" (se o DemoQA usar isso)
    public void waitColorButtonHasSuccessClass() {
        page.waitForFunction(
                "sel => {" +
                        "const el = document.querySelector(sel);" +
                        "return el && el.className.includes('text-success');" +
                        "}",
                "#colorChange",
                new Page.WaitForFunctionOptions().setTimeout(TIMEOUT_MS)
        );
    }

    // ---------------- Visible After ----------------
    public void assertVisibleAfterHiddenInitially() {
        // O elemento normalmente existe no DOM, mas está invisível inicialmente
        // Se existir e estiver hidden, assertThat(...).not().isVisible() funciona.
        // Se não existir no DOM no início, tudo bem: a espera abaixo cobre.
        if (visibleAfterBtn.count() > 0) {
            assertThat(visibleAfterBtn).not().isVisible();
        }
    }

    public void waitVisibleAfterVisible() {
        visibleAfterBtn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(TIMEOUT_MS));
        assertThat(visibleAfterBtn).isVisible();
    }
}
