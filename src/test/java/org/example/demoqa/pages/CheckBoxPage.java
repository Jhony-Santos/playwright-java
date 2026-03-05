package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckBoxPage extends BasePage {
    public CheckBoxPage(Page page) { super(page); }

    private Locator tree() {
        return page.locator(".react-checkbox-tree").first();
    }

    private void ensureOnCheckBoxPage() {
        page.waitForURL("**/checkbox", new Page.WaitForURLOptions().setTimeout(15_000));
        page.locator("div.main-header:has-text('Check Box')").first()
                .waitFor(new Locator.WaitForOptions().setTimeout(15_000));
        safeRemoveObstructions();
    }

    private void ensureTreeReady() {
        ensureOnCheckBoxPage();

        // ATTACHED é mais estável que VISIBLE para container grande
        tree().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(15_000));

        safeRemoveObstructions();
    }

    /** Expande toda a árvore. */
    public CheckBoxPage expandAll() {
        ensureTreeReady();

        Locator expandAll = page.locator(
                "button[title='Expand all'], button[aria-label='Expand all'], button.rct-option-expand-all"
        ).first();

        expandAll.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15_000));

        expandAll.scrollIntoViewIfNeeded();
        expandAll.click();
        return this;
    }

    public CheckBoxPage selectByNode(String nodeId) {
        ensureTreeReady();

        Locator input = page.locator("#tree-node-" + nodeId).first();
        input.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(10_000));

        // Clica no checkbox visual do nó (mais confiável que label[for])
        Locator checkbox = input.locator("xpath=ancestor::li[1]//span[contains(@class,'rct-checkbox')]").first();
        checkbox.scrollIntoViewIfNeeded();
        checkbox.click();

        return this;
    }

    public String resultText() {
        Locator result = page.locator("#result").first();
        result.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));
        return result.innerText();
    }

    public CheckBoxPage assertResultContains(String expected) {
        String out = resultText().toLowerCase();
        assertTrue(out.contains(expected.toLowerCase()),
                "Esperava conter '" + expected + "' em: " + out);
        return this;
    }

    public boolean isCheckedByNode(String nodeId) {
        return page.locator("#tree-node-" + nodeId).isChecked();
    }
}