package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasePage {

    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    // Melhor prática: valida carregamento por URL (regex) + âncora visível
    protected void assertLoaded(Pattern urlPattern, Locator anchor) {
        if (urlPattern != null) {
            assertThat(page).hasURL(urlPattern);
        }
        assertThat(anchor).isVisible();
    }

    // Caso você queira só âncora
    protected void assertLoaded(Locator anchor) {
        assertThat(anchor).isVisible();
    }

    // Mantém o que você já tinha
    protected void removeObstructions() {
        page.evaluate("() => {" +
                "const hide = sel => { const el = document.querySelector(sel); if (el) el.remove(); };" +
                "hide('#fixedban');" +
                "hide('footer');" +
                "}");
    }
}
