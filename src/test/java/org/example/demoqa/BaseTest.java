package org.example.demoqa;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ScreenshotType;
import org.junit.jupiter.api.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;

    protected BrowserContext context;
    protected Page page;

    // ----- FLAGS (VM options) -----
    // -Ddebug=true|false
    protected static final boolean DEBUG =
            Boolean.parseBoolean(System.getProperty("debug", "true"));

    // -Dtrace=true|false
    private static final boolean TRACE =
            Boolean.parseBoolean(System.getProperty("trace", "false"));

    // -Dvideo=true|false
    private static final boolean VIDEO =
            Boolean.parseBoolean(System.getProperty("video", "false"));

    // -Dslowmo=900  (ms)
    // se debug=true e nada for informado, assume 800ms (bom para observar)
    private static final int SLOW_MO =
            Integer.parseInt(System.getProperty("slowmo", DEBUG ? "800" : "0"));

    // -Dpause=true|false  (Playwright Inspector)
    private static final boolean PAUSE =
            Boolean.parseBoolean(System.getProperty("pause", "false"));

    // -Dhold=5000  (ms) segura no final do teste
    private static final int HOLD_MS =
            Integer.parseInt(System.getProperty("hold", DEBUG ? "1500" : "0"));

    // -DactionTimeout=30000 / -DnavTimeout=60000 (opcional)
    private static final int ACTION_TIMEOUT_MS =
            Integer.parseInt(System.getProperty("actionTimeout", DEBUG ? "30000" : "8000"));

    private static final int NAV_TIMEOUT_MS =
            Integer.parseInt(System.getProperty("navTimeout", DEBUG ? "60000" : "20000"));

    // ----- ARTIFACTS -----
    private static final Path ARTIFACTS_DIR = Paths.get("target", "artifacts");
    private static final Path TRACES_DIR = ARTIFACTS_DIR.resolve("traces");
    private static final Path SCREENSHOTS_DIR = ARTIFACTS_DIR.resolve("screenshots");
    private static final Path VIDEOS_DIR = ARTIFACTS_DIR.resolve("videos");

    @BeforeAll
    static void beforeAll() {
        playwright = Playwright.create();

        BrowserType.LaunchOptions launch = new BrowserType.LaunchOptions()
                .setHeadless(!DEBUG)
                .setSlowMo(SLOW_MO)
                .setArgs(List.of(
                        "--start-maximized",
                        "--window-size=1600,1000"
                ));

        browser = playwright.chromium().launch(launch);
    }

    @BeforeEach
    void createContext(TestInfo testInfo) throws Exception {
        Files.createDirectories(ARTIFACTS_DIR);
        Files.createDirectories(TRACES_DIR);
        Files.createDirectories(SCREENSHOTS_DIR);
        Files.createDirectories(VIDEOS_DIR);

        Browser.NewContextOptions options = new Browser.NewContextOptions()
                // viewport null -> usa tamanho da janela
                .setViewportSize(null);

        if (VIDEO) {
            options.setRecordVideoDir(VIDEOS_DIR);
        }

        context = browser.newContext(options);
        page = context.newPage();

        // Timeouts globais (boa prática: centralizados aqui)
        page.setDefaultTimeout(ACTION_TIMEOUT_MS);
        page.setDefaultNavigationTimeout(NAV_TIMEOUT_MS);

        // Tracing
        if (TRACE) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }

        // Inspector (debug avançado)
        if (PAUSE) {
            page.pause();
        }
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        try {
            // Segura no final para você ver o estado final (somente em debug)
            if (DEBUG && HOLD_MS > 0 && page != null) {
                page.waitForTimeout(HOLD_MS);
            }

            // Screenshot (em debug sempre ajuda; em trace também)
            if ((DEBUG || TRACE) && page != null) {
                String fileName = safeTestName(testInfo) + ".png";
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(SCREENSHOTS_DIR.resolve(fileName))
                        .setFullPage(true)
                        .setType(ScreenshotType.PNG));
            }

            // Trace
            if (TRACE && context != null) {
                String traceName = safeTestName(testInfo) + ".zip";
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(TRACES_DIR.resolve(traceName)));
            }
        } catch (Exception ignored) {
        } finally {
            if (context != null) context.close();
        }
    }

    @AfterAll
    static void afterAll() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    private static String safeTestName(TestInfo testInfo) {
        String className = testInfo.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
        String methodName = testInfo.getTestMethod().map(m -> m.getName()).orElse("unknownTest");
        return className + "_" + methodName;
    }
}
