package org.example.demoqa.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.example.demoqa.pages.BasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SideMenuComponent extends BasePage {

    private static final int MENU_TIMEOUT_MS =
            Integer.parseInt(System.getProperty("menuTimeout", "30000"));

    public SideMenuComponent(Page page) {
        super(page);
    }

    /** Abre um ITEM dentro de uma seção (ex.: "Text Box", "Links", "Web Tables"). */
    public void openItem(String text) {
        safeRemoveObstructions();

        Locator menuContainer = resolveMenuContainer();
        assertThat(menuContainer).isVisible();

        ensureMenuIsReady(menuContainer);

        // ✅ Preferir match exato
        Locator exact = menuContainer.locator("ul.menu-list li span:text-is('" + text + "')").first();

        Locator item;
        if (exact.count() > 0) {
            item = exact;
        } else {
            // fallback: estrutura antiga do site
            item = menuContainer.locator("ul.menu-list li span.text")
                    .filter(new Locator.FilterOptions().setHasText(text))
                    .first();
        }

        item.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(MENU_TIMEOUT_MS));

        item.scrollIntoViewIfNeeded();
        safeRemoveObstructions();

        // ✅ Click com retry (demoqa às vezes engole o primeiro)
        tryClick(item);
    }

    /**
     * Abre um HEADER de seção no accordion lateral (ex.: "Forms", "Widgets", "Interactions").
     * Isso NÃO é um item do menu-list.
     */
    public void openSection(String sectionText) {
        safeRemoveObstructions();

        Locator menuContainer = resolveMenuContainer();
        assertThat(menuContainer).isVisible();

        ensureMenuIsReady(menuContainer);

        Locator header = menuContainer.locator(".element-group")
                .filter(new Locator.FilterOptions().setHasText(sectionText))
                .locator(".header-wrapper, .group-header")
                .first();

        header.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(MENU_TIMEOUT_MS));

        header.scrollIntoViewIfNeeded();
        safeRemoveObstructions();

        tryClick(header);
    }

    private void ensureMenuIsReady(Locator menuContainer) {
        // Não depende do ensureAppIsUp() genérico (que pode falhar por hydration).
        // Aqui queremos apenas: painel visível + lista do menu presente/ativa.
        menuContainer.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(MENU_TIMEOUT_MS));

        Locator menuList = menuContainer.locator("ul.menu-list").first();
        menuList.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(MENU_TIMEOUT_MS));

        // Aguarda ter ao menos 1 item clicável (hydration terminou)
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < MENU_TIMEOUT_MS) {
            int items = menuList.locator("li span").count();
            if (items > 0) return;
            page.waitForTimeout(200);
        }

        diagnosticSnapshot("menu_not_ready");
        throw new RuntimeException("Menu lateral não ficou pronto a tempo. URL=" + safeUrl() + " title=" + safeTitle());
    }

    private void tryClick(Locator target) {
        try {
            target.click(new Locator.ClickOptions().setTimeout(MENU_TIMEOUT_MS));
            return;
        } catch (Exception first) {
            // retry curto + force (overlay / intercept)
            try {
                page.waitForTimeout(250);
                safeRemoveObstructions();
                target.click(new Locator.ClickOptions().setTimeout(MENU_TIMEOUT_MS).setForce(true));
            } catch (Exception second) {
                diagnosticSnapshot("menu_click_failed");
                throw second;
            }
        }
    }

    private Locator resolveMenuContainer() {
        Locator leftPanel = page.locator(".left-pannel, .left-panel").first();
        try {
            leftPanel.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(MENU_TIMEOUT_MS));
            return leftPanel;
        } catch (Exception ignored) { }

        Locator accordion = page.locator(".accordion").first();
        accordion.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(MENU_TIMEOUT_MS));
        return accordion;
    }
}