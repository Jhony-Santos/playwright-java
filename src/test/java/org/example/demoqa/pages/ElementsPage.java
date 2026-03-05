package org.example.demoqa.pages;

import com.microsoft.playwright.Page;
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
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
    }

    public CheckBoxPage openCheckBox() {
        page.navigate("https://demoqa.com/checkbox",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.LOAD));

        page.waitForURL("**/checkbox", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();

        // ✅ Ajuda bastante em SPA: espera a rede "assentar"
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE,
                new Page.WaitForLoadStateOptions().setTimeout(30_000));

        // ✅ Confirma rota por texto em qualquer anchor (precisa existir no seu BasePage)
        waitForTextAny("div.main-header, h1, h2, header, .main-content", "Check Box", 60_000);

        // ✅ Espera o componente aparecer (polling do DOM)
        page.waitForFunction(
                "() => document.querySelector('.react-checkbox-tree') !== null",
                null,
                new Page.WaitForFunctionOptions().setTimeout(60_000)
        );

        // ✅ Espera hidratação (labels renderizados)
        page.waitForFunction(
                "() => document.querySelectorAll('.react-checkbox-tree label').length > 0",
                null,
                new Page.WaitForFunctionOptions().setTimeout(60_000)
        );

        safeRemoveObstructions();
        return new CheckBoxPage(page);
    }

    // ------------------ Mantive seus demais métodos ------------------

    public TextBoxPage openTextBox() {
        page.navigate("https://demoqa.com/text-box",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/text-box", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
        return new TextBoxPage(page);
    }

    public LinksPage openLinks() {
        page.navigate("https://demoqa.com/links",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/links", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
        return new LinksPage(page);
    }

    public UploadDownloadPage openUploadDownload() {
        page.navigate("https://demoqa.com/upload-download",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/upload-download", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
        return new UploadDownloadPage(page);
    }

    public WebTablesPage openWebTables() {
        page.navigate("https://demoqa.com/webtables",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/webtables", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
        return new WebTablesPage(page);
    }

    public ButtonsPage openButtons() {
        page.navigate("https://demoqa.com/buttons",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/buttons", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
        return new ButtonsPage(page);
    }

    public RadioButtonPage openRadioButton() {
        page.navigate("https://demoqa.com/radio-button",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/radio-button", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
        return new RadioButtonPage(page);
    }

    public DynamicPropertiesPage openDynamicProperties() {
        page.navigate("https://demoqa.com/dynamic-properties",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/dynamic-properties", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
        return new DynamicPropertiesPage(page);
    }

    public BrokenLinksImagesPage openBrokenLinksImages() {
        page.navigate("https://demoqa.com/broken",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/broken", new Page.WaitForURLOptions().setTimeout(30_000));
        safeRemoveObstructions();
        ensureAppIsUp(List.of("div.main-header"), 60_000, true);
        return new BrokenLinksImagesPage(page);
    }

    public ElementsPage openItemViaSideMenu(String itemText) {
        ensureOnElements();
        sideMenu.openItem(itemText);
        return this;
    }
}