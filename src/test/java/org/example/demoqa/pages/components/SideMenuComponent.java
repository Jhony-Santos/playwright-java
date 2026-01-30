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

    public void openItem(String text) {
        // 0) Limpa overlays e dá “respiro”
        removeObstructions();
        stepDelay();

        // 1) Garante que a página “montou” ao menos o #app (root SPA)
        Locator appRoot = page.locator("#app");
        appRoot.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(MENU_TIMEOUT_MS));

        // 2) Resolve o container do menu (left-pannel preferencial, senão accordion)
        Locator menuContainer = resolveMenuContainer();
        assertThat(menuContainer).isVisible();

        // 3) Resolve a lista do menu
        Locator menuList = menuContainer.locator("ul.menu-list").first();
        menuList.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(MENU_TIMEOUT_MS));

        // 4) Acha o item pelo texto (span.text)
        Locator itemText = menuList.locator("li span.text")
                .filter(new Locator.FilterOptions().setHasText(text))
                .first();

        itemText.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(MENU_TIMEOUT_MS));

        itemText.scrollIntoViewIfNeeded();
        removeObstructions();
        stepDelay();

        // 5) Clique mais confiável: no próprio span.text
        // (clicar no LI às vezes não dispara navegação dependendo do handler)
        highlight(itemText);
        stepDelay();

        try {
            itemText.click(new Locator.ClickOptions().setTimeout(MENU_TIMEOUT_MS));
        } catch (Exception e) {
            // fallback defensivo
            itemText.click(new Locator.ClickOptions().setForce(true).setTimeout(MENU_TIMEOUT_MS));
        }

        stepDelay();
    }

    private Locator resolveMenuContainer() {
        // Preferência: menu lateral padrão da tela /elements
        Locator leftPanel = page.locator(".left-pannel, .left-panel").first();

        // 1) espera ATTACHED (não exige visível ainda)
        try {
            leftPanel.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(MENU_TIMEOUT_MS));

            // 2) agora exige VISIBLE
            leftPanel.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(MENU_TIMEOUT_MS));

            return leftPanel;
        } catch (Exception ignored) {
            // fallback: alguns layouts renderizam via accordion
        }

        Locator accordion = page.locator(".accordion").first();
        accordion.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(MENU_TIMEOUT_MS));
        return accordion;
    }
}
