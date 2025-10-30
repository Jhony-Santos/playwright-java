package org.example.demoqa;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void beforeAll() {
        playwright = Playwright.create();
        // deixe visível para enxergar o site; mude para true quando quiser headless
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(600)
        );
    }

    @AfterAll
    static void afterAll() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1600, 1000) // largura suficiente para mostrar o left-pannel
        );
        page = context.newPage();
        page.setDefaultTimeout(10000);// opcional: 10s p/ acelerar feedback
    }

    @AfterEach
    void closeContext() {
        if (context != null) context.close();
    }





}
