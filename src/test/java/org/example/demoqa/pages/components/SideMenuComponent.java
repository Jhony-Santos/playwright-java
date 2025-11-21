package org.example.demoqa.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SideMenuComponent {

    private final Page page;

    public SideMenuComponent(Page page) {
        this.page = page;
    }

    public void openItem(String text) {

        // cada item é um <li class="btn btn-light"> com um <span class="text">Text Box</span>
        Locator item = page.locator("ul.menu-list li.btn.btn-light").filter(
                new Locator.FilterOptions().setHasText(text)
        );

        item.first().scrollIntoViewIfNeeded();
        item.first().click();
    }
}
