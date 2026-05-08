package org.example.demoqa.pages;

import com.microsoft.playwright.Page;

public class BookDetailsPage extends BasePage {

    public BookDetailsPage(Page page) {
        super(page);
    }

    public BookDetailsPage addToCollection() {
        page.onceDialog(dialog -> dialog.accept());

        click(page.locator("button:has-text('Add To Your Collection')"));

        return this;
    }
}