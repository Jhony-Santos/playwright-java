package org.example.demoqa.pages.components;

import com.microsoft.playwright.Page;

public class SideMenuComponent {
    private final Page page;

    public SideMenuComponent(Page page) {
        this.page = page;
    }

    /** Clica em um item do menu lateral pelo texto exibido (match exato). */
    public void openItem(String text) {
        page.locator(".left-pannel :text-is(\"" + text + "\")").click();
    }
}
