package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoogleTest {
    static Playwright playwright;
    static Browser browser;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(250));
    }

    @AfterAll
    static void teardown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    void deveAbrirGoogleEValidarTitulo() {
        Page page = browser.newPage();
        page.navigate("https://www.google.com/");
        assertTrue(page.title().contains("Google"));
        page.waitForTimeout(2000);
        page.close();
    }
}
