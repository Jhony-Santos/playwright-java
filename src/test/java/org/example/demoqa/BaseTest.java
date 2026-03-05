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

    // Flags:
    // -Dheadless=true|false
    // -DdebugNet=true   (loga rede/console)
    private static final boolean DEBUG_NET =
            Boolean.parseBoolean(System.getProperty("debugNet", "false"));

    // Se quiser ser mais estrito e só logar do demoqa:
    private static final String APP_HOST = "demoqa.com";

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

        // Bloqueia ads/trackers (silencioso por padrão)
        context.route("**/*", route -> {
            String url = route.request().url();

            boolean block = isNoisyThirdParty(url);
            if (block) {
                if (DEBUG_NET) System.out.println("[route:abort] " + url);
                route.abort();
            } else {
                route.resume();
            }
        });

        page = context.newPage();

        // Console: só loga se debugNet=true, e filtra ruído de ads/trackers
        page.onConsoleMessage(msg -> {
            String type = msg.type();
            if (!("warning".equals(type) || "error".equals(type))) return;

            String text = msg.text();

            // ruído típico quando você bloqueia ads:
            if (isNoisyConsole(text)) {
                if (DEBUG_NET) System.out.println("[console:" + type + "][filtered] " + text);
                return;
            }

            if (DEBUG_NET) {
                System.out.println("[console:" + type + "] " + text);
            }
        });

        // PageError: costuma indicar bug real. Loga sempre, mas pode deixar condicionado ao debugNet se preferir.
        page.onPageError(err -> System.out.println("[pageerror] " + err));

        // requestfailed: não loga falha de request de third-party que você mesmo bloqueou
        page.onRequestFailed(req -> {
            String url = req.url();
            String failure = req.failure() != null ? req.failure() : "";

            // se for terceiro + erro típico de bloqueio, ignora
            if (isNoisyThirdParty(url) && (failure.contains("ERR_FAILED") || failure.contains("ERR_ABORTED"))) {
                return;
            }

            // opcional: não logar assets abortados
            if (isNoisyAsset(url) && (failure.contains("ERR_ABORTED") || failure.contains("ERR_FAILED"))) {
                return;
            }

            if (DEBUG_NET) {
                System.out.println("[requestfailed] " + req.method() + " " + url + " -> " + failure);
            }
        });

        // HTTP 4xx/5xx: loga só do app (demoqa) — isso sim é “sinal”
        page.onResponse(res -> {
            int status = res.status();
            if (status >= 400) {
                String url = res.url();
                if (isAppUrl(url)) {
                    System.out.println("[http " + status + "] " + url);
                } else if (DEBUG_NET) {
                    // se quiser ver terceiros em debug
                    System.out.println("[http " + status + "][third-party] " + url);
                }
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

    // ------------------- filtros -------------------

    private boolean isAppUrl(String url) {
        return url != null && url.contains(APP_HOST);
    }

    private boolean isNoisyThirdParty(String url) {
        if (url == null) return false;
        return url.contains("doubleclick.net")
                || url.contains("securepubads.g.doubleclick.net")
                || url.contains("googlesyndication.com")
                || url.contains("google-analytics.com")
                || url.contains("googletagmanager.com")
                || url.contains("googletagservices.com")
                || url.contains("pagead2.googlesyndication.com")
                || url.contains("stats.g.doubleclick.net")
                || url.contains("analytics.google.com")
                || url.contains("dmtry.com")
                || url.contains("spotxchange.com");
    }

    private boolean isNoisyAsset(String url) {
        if (url == null) return false;
        // assets comuns que não valem o spam no log
        return url.matches(".*\\.(svg|png|jpg|jpeg|gif|woff2?|ttf|eot)($|\\?).*");
    }

    private boolean isNoisyConsole(String text) {
        if (text == null) return false;
        // mensagens típicas quando bloqueia ads/trackers
        boolean hasNetErr = text.contains("net::ERR_FAILED") || text.contains("net::ERR_ABORTED");
        boolean failedToLoad = text.contains("Failed to load resource");

        if (!(hasNetErr || failedToLoad)) return false;

        // se menciona domínios conhecidos de ads/trackers, é ruído
        return text.contains("googletagmanager")
                || text.contains("doubleclick")
                || text.contains("googlesyndication")
                || text.contains("securepubads")
                || text.contains("googletagservices")
                || text.contains("pagead2");
    }
}