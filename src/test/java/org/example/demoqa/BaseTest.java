package org.example.demoqa;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    // Flag para modo debug (padrão = true)
    private static final boolean DEBUG =
            Boolean.parseBoolean(System.getProperty("debug", "true"));

    @BeforeAll
    static void beforeAll() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(!DEBUG)             // janela visível só em debug
                        .setSlowMo(DEBUG ? 800 : 0)      // mais lento pra enxergar
                        // abre a janela maximizada no monitor atual
                        .setArgs(List.of("--start-maximized"))
        );
    }

    @BeforeEach
    void createContext() {
        // 👇 MUITO IMPORTANTE:
        // null => Playwright NÃO força um viewport fixo
        // e passa a usar exatamente o tamanho da janela (maximizada)
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(null)
        );

        page = context.newPage();

        page.setDefaultTimeout(DEBUG ? 15000 : 8000);
        page.setDefaultNavigationTimeout(DEBUG ? 45000 : 20000);
    }

    @AfterEach
    void closeContext() {
        if (context != null) context.close();
    }

    @AfterAll
    static void afterAll() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
