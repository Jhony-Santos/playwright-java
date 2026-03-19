package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;
import org.example.demoqa.pages.components.SideMenuComponent;
import java.util.List;



public class ElementsPage extends BasePage {

    private final SideMenuComponent sideMenu;

    public ElementsPage(Page page) {
        super(page);
        this.sideMenu = new SideMenuComponent(page);
    }

    private void ensureOnElements() {
        page.waitForURL("**/elements", new Page.WaitForURLOptions().setTimeout(20_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
    }

    public CheckBoxPage openCheckBox() {
        page.navigate("https://demoqa.com/checkbox",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        page.waitForURL("**/checkbox", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();

        // Não dependa de networkidle (ads/gtm vão te enganar)
        try {
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED,
                    new Page.WaitForLoadStateOptions().setTimeout(10_000));
        } catch (Exception ignored) {}

        try {
            // 1) Confirma que estamos na página certa
            waitForTextAny("h1, h2, header, .main-content, body", "Check Box", 30_000);

            // 2) Espera o container real do componente
            Locator treeWrapper = page.locator(".check-box-tree-wrapper").first();
            treeWrapper.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(60_000));

            // 3) Espera o rc-tree montar e ter pelo menos 1 nó
            Locator rcTree = page.locator(".check-box-tree-wrapper .rc-tree").first();
            rcTree.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(60_000));

            page.waitForFunction(
                    "() => document.querySelectorAll('.rc-tree .rc-tree-treenode').length > 0",
                    null,
                    new Page.WaitForFunctionOptions().setTimeout(60_000)
            );

            // 4) Garante que o nó "Home" existe (texto)
            page.waitForFunction(
                    "() => Array.from(document.querySelectorAll('.rc-tree-title')).some(e => (e.textContent||'').trim() === 'Home')",
                    null,
                    new Page.WaitForFunctionOptions().setTimeout(60_000)
            );

            safeRemoveObstructions();
            return new CheckBoxPage(page);

        } catch (RuntimeException e) {
            diagnosticSnapshot("checkbox_not_ready");

            String visibleText = "<unable_to_read>";
            try {
                visibleText = (String) page.evaluate(
                        "() => (document.body ? (document.body.innerText || '') : '')"
                );
                if (visibleText != null && visibleText.length() > 600) {
                    visibleText = visibleText.substring(0, 600) + "...";
                }
            } catch (Exception ignored) {}

            throw new RuntimeException(
                    "Falha ao abrir CheckBox. URL=" + safeUrl()
                            + " title=" + safeTitle()
                            + " visibleTextSnippet=" + visibleText,
                    e
            );
        }
    }

    // ------------------ Mantive seus demais métodos ------------------

    public TextBoxPage openTextBox() {
        page.navigate("https://demoqa.com/text-box",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/text-box", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
        return new TextBoxPage(page);
    }

    public LinksPage openLinks() {
        page.navigate("https://demoqa.com/links",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/links", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
        return new LinksPage(page);
    }

    public UploadDownloadPage openUploadDownload() {
        page.navigate("https://demoqa.com/upload-download",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/upload-download", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
        return new UploadDownloadPage(page);
    }

    public WebTablesPage openWebTables() {
        page.navigate("https://demoqa.com/webtables",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/webtables", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
        return new WebTablesPage(page);
    }

    public ButtonsPage openButtons() {
        page.navigate("https://demoqa.com/buttons",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/buttons", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
        return new ButtonsPage(page);
    }

    public RadioButtonPage openRadioButton() {
        page.navigate("https://demoqa.com/radio-button",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/radio-button", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
        return new RadioButtonPage(page);
    }

    public DynamicPropertiesPage openDynamicProperties() {
        page.navigate("https://demoqa.com/dynamic-properties",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/dynamic-properties", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
        return new DynamicPropertiesPage(page);
    }

    public BrokenLinksImagesPage openBrokenLinksImages() {
        page.navigate("https://demoqa.com/broken",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/broken", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);
        return new BrokenLinksImagesPage(page);
    }

    public ElementsPage openItemViaSideMenu(String itemText) {
        ensureOnElements();
        sideMenu.openItem(itemText);
        return this;
    }
}