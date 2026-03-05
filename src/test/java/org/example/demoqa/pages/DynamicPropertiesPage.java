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
        removeObstructions();

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

    // ---------------- Visible After ----------------
    public void assertVisibleAfterHiddenInitially() {
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
