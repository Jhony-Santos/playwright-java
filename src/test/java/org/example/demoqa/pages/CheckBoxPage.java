package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckBoxPage extends BasePage {
    public CheckBoxPage(Page page) { super(page); }

    private Locator tree() {
        return page.locator(".check-box-tree-wrapper .rc-tree").first();
    }

    private void ensureOnCheckBoxPage() {
        page.waitForURL("**/checkbox", new Page.WaitForURLOptions().setTimeout(30_000));
        // O site atual usa <h1 class="text-center">Check Box</h1>
        page.locator("h1:has-text('Check Box')").first()
                .waitFor(new Locator.WaitForOptions().setTimeout(30_000));
        safeRemoveObstructions();
    }

    private void ensureTreeReady() {
        ensureOnCheckBoxPage();

        tree().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(60_000));

        page.waitForFunction(
                "() => document.querySelectorAll('.rc-tree .rc-tree-treenode').length > 0",
                null,
                new Page.WaitForFunctionOptions().setTimeout(60_000)
        );

        safeRemoveObstructions();
    }

    /** Expande toda a árvore. */
    public CheckBoxPage expandAll() {
        ensureTreeReady();

        // No layout atual não existe "Expand all" button.
        // Então expandimos clicando nos switchers fechados até não sobrar nenhum.
        for (int i = 0; i < 25; i++) {
            int clicked = (int) page.evaluate("""
                () => {
                  const list = Array.from(document.querySelectorAll('.rc-tree-switcher_close'));
                  if (!list.length) return 0;
                  list.forEach(el => el.click());
                  return list.length;
                }
            """);

            if (clicked == 0) break;
            page.waitForTimeout(150);
        }

        return this;
    }

    /**
     * Mantive sua assinatura: selectByNode("desktop")
     * Aqui "desktop" é interpretado como TEXTO do nó (Desktop), case-insensitive.
     */
    public CheckBoxPage selectByNode(String nodeText) {
        ensureTreeReady();

        String wanted = nodeText == null ? "" : nodeText.trim().toLowerCase();

        // Clica no checkbox pelo aria-label "Select <Texto>"
        // Ex.: "Select Desktop"
        Locator checkbox = page.locator(".rc-tree-checkbox[aria-label]")
                .filter(new Locator.FilterOptions().setHasText("")) // só pra permitir filter chain
                .first();

        // Encontrar o checkbox certo via JS (mais determinístico)
        boolean ok = (boolean) page.evaluate("""
            (wanted) => {
              const nodes = Array.from(document.querySelectorAll('.rc-tree-treenode'));
              const norm = s => (s || '').trim().toLowerCase();
              for (const n of nodes) {
                const title = n.querySelector('.rc-tree-title');
                if (!title) continue;
                if (norm(title.textContent) !== wanted) continue;

                const cb = n.querySelector('.rc-tree-checkbox[aria-label]');
                if (!cb) return false;
                cb.click();
                return true;
              }
              return false;
            }
        """, wanted);

        assertTrue(ok, "Não encontrei o nó '" + nodeText + "' na árvore rc-tree.");
        return this;
    }

    // OBS: No layout atual dessa página, não existe #result como no demo antigo.
    // Se você tiver #result em algum build, mantenha. Se não, recomendo validar o aria-checked do checkbox.
    public boolean isCheckedByNode(String nodeText) {
        String wanted = nodeText == null ? "" : nodeText.trim().toLowerCase();

        return (boolean) page.evaluate("""
            (wanted) => {
              const norm = s => (s || '').trim().toLowerCase();
              const nodes = Array.from(document.querySelectorAll('.rc-tree-treenode'));
              for (const n of nodes) {
                const title = n.querySelector('.rc-tree-title');
                if (!title) continue;
                if (norm(title.textContent) !== wanted) continue;

                const cb = n.querySelector('.rc-tree-checkbox');
                if (!cb) return false;
                // quando marcado, rc-tree costuma adicionar a classe rc-tree-checkbox-checked
                return cb.classList.contains('rc-tree-checkbox-checked')
                    || cb.getAttribute('aria-checked') === 'true';
              }
              return false;
            }
        """, wanted);
    }

    public CheckBoxPage assertResultContains(String expected) {
        // Se você ainda tiver #result no seu site/build, você pode reativar essa lógica.
        // No seu HTML atual, NÃO há #result. Então validamos estado do checkbox.
        assertTrue(isCheckedByNode(expected),
                "Esperava que o nó '" + expected + "' estivesse marcado, mas não está.");
        return this;
    }
}