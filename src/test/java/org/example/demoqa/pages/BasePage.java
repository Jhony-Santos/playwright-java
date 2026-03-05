package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasePage {

    protected final Page page;

    // Use: -DstepDelayMs=300
    private static final int STEP_DELAY_MS =
            Integer.parseInt(System.getProperty("stepDelayMs", "150"));

    public BasePage(Page page) {
        this.page = page;
    }

    protected String safeUrl() {
        try { return page.url(); } catch (Exception e) { return "<no-url>"; }
    }

    protected String safeTitle() {
        try { return page.title(); } catch (Exception e) { return "<no-title>"; }
    }

    /**
     * IMPORTANTÍSSIMO:
     * NÃO use seletores genéricos tipo div[class*='ads'].
     * Isso pode remover elementos legítimos do site.
     */
    protected void safeRemoveObstructions() {
        try {
            page.evaluate("""
                () => {
                  const selectors = [
                    // banner fixo conhecido do demoqa
                    "#fixedban",

                    // ads comuns (assinatura clara)
                    "iframe[id^='google_ads']",
                    "iframe[src*='doubleclick']",
                    "iframe[src*='googlesyndication']",
                    "ins.adsbygoogle",
                    "div[id*='google_ads']",
                    "div[id^='google_ads']"
                  ];

                  for (const sel of selectors) {
                    document.querySelectorAll(sel).forEach(e => e.remove());
                  }
                }
            """);
        } catch (Exception ignored) {}
    }

    protected void diagnosticSnapshot(String prefix) {
        try {
            Path dir = Paths.get("target", "diagnostics");
            Files.createDirectories(dir);

            long ts = Instant.now().toEpochMilli();

            Path png = dir.resolve(prefix + "_" + ts + ".png");
            Path html = dir.resolve(prefix + "_" + ts + ".html");

            page.screenshot(new Page.ScreenshotOptions().setPath(png).setFullPage(true));
            Files.writeString(html, page.content(), StandardCharsets.UTF_8);
        } catch (Exception ignored) {}
    }

    protected void ensureAppIsUp() {
        ensureAppIsUp(List.of("body"));
    }

    protected void ensureAppIsUp(List<String> anchors) {
        ensureAppIsUp(anchors, 45_000, true);
    }

    /**
     * Espera baseada em DOM/UI:
     * - Roots SPA: ATTACHED
     * - UI: VISIBLE
     *
     * E SEM ficar removendo coisas em loop.
     */
    protected void ensureAppIsUp(List<String> anchors, long timeoutMs, boolean allowOneReload) {
        RuntimeException last = null;

        for (int attempt = 1; attempt <= 2; attempt++) {
            long attemptStart = System.currentTimeMillis();

            try {
                safeRemoveObstructions();

                for (String sel : anchors) {
                    long elapsed = System.currentTimeMillis() - attemptStart;
                    long remaining = Math.max(1_000, timeoutMs - elapsed);

                    Locator loc = page.locator(sel).first();

                    WaitForSelectorState state = isSpaRoot(sel)
                            ? WaitForSelectorState.ATTACHED
                            : WaitForSelectorState.VISIBLE;

                    loc.waitFor(new Locator.WaitForOptions()
                            .setState(state)
                            .setTimeout(remaining));
                }

                safeRemoveObstructions();
                return;

            } catch (RuntimeException e) {
                last = e;
                diagnosticSnapshot("app_not_ready_attempt_" + attempt);

                if (allowOneReload && attempt == 1) {
                    try {
                        page.reload(new Page.ReloadOptions());
                        page.waitForTimeout(500);
                    } catch (Exception ignored) {}
                    continue;
                }
                break;
            }
        }

        throw new RuntimeException(
                "App não montou a UI a tempo. URL: " + safeUrl()
                        + " title=" + safeTitle()
                        + " cause=" + (last != null ? last.getMessage() : "<unknown>"),
                last
        );
    }

    private boolean isSpaRoot(String sel) {
        if (sel == null) return false;
        String s = sel.trim().toLowerCase();
        return s.equals("#app") || s.equals("#root") || s.contains("#app") || s.contains("#root");
    }

    // ---------------- Helpers ----------------

    protected void stepDelay() {
        if (STEP_DELAY_MS > 0) page.waitForTimeout(STEP_DELAY_MS);
    }

    protected void highlight(Locator locator) {
        try {
            locator.evaluate("el => { el.style.outline = '3px solid magenta'; el.style.outlineOffset = '2px'; }");
        } catch (Exception ignored) {}
    }

    protected void assertLoaded(Pattern urlPattern, Locator... anchors) {
        assertThat(page).hasURL(urlPattern);
        for (Locator a : anchors) assertThat(a).isVisible();
    }

    protected void click(Locator locator) {
        safeRemoveObstructions();
        locator.scrollIntoViewIfNeeded();

        try {
            locator.click(new Locator.ClickOptions().setTimeout(30_000));
        } catch (Exception e) {
            safeRemoveObstructions();
            locator.click(new Locator.ClickOptions().setTimeout(30_000).setForce(true));
        }
    }

    protected void removeObstructions() { safeRemoveObstructions(); }
    protected void removeObstructionsSafe() { safeRemoveObstructions(); }

    /**
     * Espera um selector existir (ATTACHED) e conter um texto (case-insensitive),
     * sem depender do critério "visible".
     */
    protected void waitForText(String selector, String expectedSubstring, long timeoutMs) {
        try {
            Locator loc = page.locator(selector).first();

            // 1) garante que existe no DOM
            loc.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(timeoutMs / 2)); // metade para não gastar tudo aqui

            // 2) garante que o texto chegou (sem depender de "visible")
            Map<String, Object> args = Map.of(
                    "sel", selector,
                    "txt", expectedSubstring.toLowerCase()
            );

            page.waitForFunction(
                    "({sel, txt}) => {" +
                            "const el = document.querySelector(sel);" +
                            "if (!el) return false;" +
                            "const t = (el.textContent || '').toLowerCase();" +
                            "return t.includes(txt);" +
                            "}",
                    args,
                    new Page.WaitForFunctionOptions().setTimeout(timeoutMs / 2)
            );

        } catch (RuntimeException e) {
            diagnosticSnapshot("waitForText_failed_" + selector.replaceAll("[^a-zA-Z0-9]+", "_"));

            Locator fallback = page.locator(".main-content, .body-height, .container").first();
            try {
                fallback.waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(10_000));
            } catch (Exception ignored) {}

            throw e;
        }
    }

    /**
     * Espera o texto aparecer em QUALQUER elemento que bata no seletor (case-insensitive).
     * (Implementação usando Map para evitar problemas de overload/assinatura no Java.)
     */
    protected void waitForTextAny(String selector, String expectedSubstring, long timeoutMs) {
        Map<String, Object> args = Map.of(
                "sel", selector,
                "txt", expectedSubstring.toLowerCase()
        );

        page.waitForFunction(
                "({sel, txt}) => {" +
                        "const nodes = Array.from(document.querySelectorAll(sel));" +
                        "if (!nodes.length) return false;" +
                        "return nodes.some(n => (n.textContent || '').toLowerCase().includes(txt));" +
                        "}",
                args,
                new Page.WaitForFunctionOptions().setTimeout(timeoutMs)
        );
    }
}