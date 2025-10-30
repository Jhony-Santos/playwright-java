package org.example.demoqa.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class SideMenuComponent {
    private final Page page;

    public SideMenuComponent(Page page) {
        this.page = page;
    }

    /**
     * Clica em um item do menu lateral pelo texto exibido (match exato),
     * esperando ele estar visível e pronto para interação.
     */

    public void openItem(String text) {
        Locator item = page.locator(".left-pannel :text-is('" + text + "')");
        item.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        item.scrollIntoViewIfNeeded();
        item.click();
    }
}
