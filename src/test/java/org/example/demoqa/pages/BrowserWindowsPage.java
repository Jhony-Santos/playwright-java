package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BrowserWindowsPage extends BasePage {

    // botões da página
    private final Locator newTabBtn;
    private final Locator newWindowBtn;
    private final Locator newWindowMsgBtn;

    public BrowserWindowsPage(Page page) {
        super(page);
        this.newTabBtn = page.locator("#tabButton");
        this.newWindowBtn = page.locator("#windowButton");
        this.newWindowMsgBtn = page.locator("#messageWindowButton");
    }

    /** Clica em New Tab e retorna a página/tabela aberta (popup). */
    public Page openNewTab() {
        return page.waitForPopup(() -> newTabBtn.click());
    }

    /** Clica em New Window e retorna a janela aberta (popup). */
    public Page openNewWindow() {
        return page.waitForPopup(() -> newWindowBtn.click());
    }

    /**
     * Clica em New Window Message e retorna o popup.
     * Esse popup exibe um texto no body (sem H1), então
     * geralmente vamos ler o innerText do <body>.
     */
    public Page openNewWindowMessage() {
        return page.waitForPopup(() -> newWindowMsgBtn.click());
    }
}
