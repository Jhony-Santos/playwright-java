package org.example.demoqa;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    protected final Path downloadsDir = Paths.get("target", "downloads");
    protected final Path diagnosticsDir = Paths.get("target", "diagnostics");

    @BeforeEach
    void setUp() throws Exception {
        playwright = Playwright.create();

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        List<String> chromiumArgs = List.of(
                "--disable-gpu",
                "--disable-dev-shm-usage",
                "--no-sandbox"
        );

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setArgs(chromiumArgs));

        Files.createDirectories(downloadsDir);
        Files.createDirectories(diagnosticsDir);

        context = browser.newContext(new Browser.NewContextOptions()
                .setAcceptDownloads(true)
                .setViewportSize(1280, 720));

        // ✅ Bloqueia ads/trackers — OK gerar ERR_FAILED/ERR_ABORTED nos logs.
        // ✅ Loga o que foi abortado para garantir que não bloqueamos algo indevido.
        context.route("**/*", route -> {
            String url = route.request().url();

            boolean block =
                    url.contains("doubleclick.net")
                            || url.contains("securepubads.g.doubleclick.net")
                            || url.contains("googlesyndication.com")
                            || url.contains("google-analytics.com")
                            || url.contains("googletagmanager.com")
                            || url.contains("stats.g.doubleclick.net")
                            || url.contains("analytics.google.com")
                            || url.contains("dmtry.com")
                            || url.contains("spotxchange.com");

            if (block) {
                System.out.println("[route:abort] " + url);
                route.abort();
            } else {
                route.resume();
            }
        });

        page = context.newPage();

        page.onConsoleMessage(msg -> {
            if ("warning".equals(msg.type()) || "error".equals(msg.type())) {
                System.out.println("[console:" + msg.type() + "] " + msg.text());
            }
        });

        page.onPageError(err -> System.out.println("[pageerror] " + err));

        page.onRequestFailed(req ->
                System.out.println("[requestfailed] " + req.method() + " " + req.url() + " -> " + req.failure())
        );

        page.onResponse(res -> {
            int status = res.status();
            if (status >= 400) {
                System.out.println("[http " + status + "] " + res.url());
            }
        });

        page.setDefaultTimeout(45_000);
        page.setDefaultNavigationTimeout(45_000);
    }

    @AfterEach
    void tearDown() {
        try { if (page != null) page.close(); } catch (Exception ignored) {}
        try { if (context != null) context.close(); } catch (Exception ignored) {}
        try { if (browser != null) browser.close(); } catch (Exception ignored) {}
        try { if (playwright != null) playwright.close(); } catch (Exception ignored) {}
    }
}