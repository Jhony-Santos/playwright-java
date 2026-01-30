package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasePage {

    protected final Page page;

    // Use: -DstepDelay=1500 (ms)
    private static final int STEP_DELAY_MS =
            Integer.parseInt(System.getProperty("stepDelay", "0"));

    // Use: -Dhighlight=true|false
    private static final boolean HIGHLIGHT =
            Boolean.parseBoolean(System.getProperty("highlight", "true"));

    // Use: -DhighlightMs=900
    private static final int HIGHLIGHT_MS =
            Integer.parseInt(System.getProperty("highlightMs", "900"));

    public BasePage(Page page) {
        this.page = page;
    }

    protected void assertLoaded(Pattern urlPattern, Locator anchor) {
        if (urlPattern != null) {
            assertThat(page).hasURL(urlPattern);
        }
        assertThat(anchor).isVisible();
    }

    protected void assertLoaded(Locator anchor) {
        assertThat(anchor).isVisible();
    }


    protected void removeObstructions() {
        page.evaluate("() => {" +
                "const hide = (sel) => document.querySelectorAll(sel).forEach(el => {" +
                "  el.style.setProperty('display','none','important');" +
                "  el.style.setProperty('visibility','hidden','important');" +
                "  el.style.setProperty('pointer-events','none','important');" +
                "});" +
                "hide('#fixedban');" +
                "hide('footer');" +
                "hide('iframe');" +                // ads/iframes às vezes atrapalham
                "hide('.adsbygoogle');" +          // comum em páginas com ads
                "hide('[id*=\"google\"]');" +      // defensivo
                "hide('[class*=\"google\"]');" +
                "}");
    }




    /**
     * Tempo de “leitura” entre etapas (somente se -DstepDelay > 0).
     */
    protected void stepDelay() {
        if (STEP_DELAY_MS > 0) {
            page.waitForTimeout(STEP_DELAY_MS);
        }
    }

    /**
     * Highlight correto para Playwright Java:
     * O "el" já é passado automaticamente, e você passa apenas argumentos extras (ms).
     */
    protected void highlight(Locator locator) {
        if (!HIGHLIGHT) return;

        try {
            locator.evaluate("(el, ms) => {" +
                    "const prevOutline = el.style.outline;" +
                    "const prevOffset = el.style.outlineOffset;" +
                    "el.style.outline = '3px solid #ff3b30';" +
                    "el.style.outlineOffset = '4px';" +
                    "setTimeout(() => {" +
                    "  el.style.outline = prevOutline;" +
                    "  el.style.outlineOffset = prevOffset;" +
                    "}, ms);" +
                    "}", HIGHLIGHT_MS);
        } catch (Exception ignored) {
            // Se quiser depurar, logue aqui.
        }
    }

    /**
     * Click observável (boa prática):
     * - espera estar visível
     * - scroll
     * - highlight
     * - stepDelay antes e depois
     */
    protected void click(Locator locator) {
        assertThat(locator).isVisible();
        locator.scrollIntoViewIfNeeded();
        highlight(locator);
        stepDelay();
        locator.click();
        stepDelay();
    }
}
