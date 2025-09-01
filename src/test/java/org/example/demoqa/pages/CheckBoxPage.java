package org.example.demoqa.pages;

import com.microsoft.playwright.Page;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckBoxPage extends BasePage {
    public CheckBoxPage(Page page) { super(page); }

    /** Expande toda a árvore (mais robusto que clicar no primeiro Toggle). */
    public CheckBoxPage expandAll() {
        page.locator("button[title='Expand all']").click();
        return this;
    }

    /**
     * Marca um nó pelo id do input (estável).
     * Exemplos de nodeId: "desktop", "documents", "downloads", "notes", "commands", "react"...
     */
    public CheckBoxPage selectByNode(String nodeId) {
        page.locator("label[for='tree-node-" + nodeId + "']").click();
        return this;
    }

    /** Lê o texto de resultado (ex.: “You have selected : desktop …”). */
    public String resultText() {
        return page.locator("#result").innerText();
    }

    /** Helper de asserção para manter o teste limpo. */
    public CheckBoxPage assertResultContains(String expected) {
        String out = resultText().toLowerCase();
        assertTrue(out.contains(expected.toLowerCase()),
                "Esperava conter '" + expected + "' em: " + out);
        return this;
    }

    /** (Opcional) Verifica o estado diretamente no input. */
    public boolean isCheckedByNode(String nodeId) {
        return page.locator("#tree-node-" + nodeId).isChecked();
    }
}
