package org.example.demoqa.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class BasePage {

    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void clickHeading(String name) {
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(name)).click();
    }


    protected void clickMenuByText(String text) {
        page.getByText(text,new Page.GetByTextOptions().setExact(true)).click();
    }

    // BasePage.java
    protected void removeObstructions() {
        page.evaluate("() => {" +
                "const hide = sel => { const el = document.querySelector(sel); if (el) el.remove(); };" +
                "hide('#fixedban');" +     // remove banner fixo de anúncios
                "hide('footer');" +        // remove rodapé que às vezes cobre botões
                "}");
    }




}
