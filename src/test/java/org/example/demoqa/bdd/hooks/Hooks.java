package org.example.demoqa.bdd.hooks;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.example.demoqa.bdd.World;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hooks {
    private final World w;

    public Hooks(World world) { this.w = world; }

    @Before
    public void setUp() {
        w.playwright = Playwright.create();
        w.browser = w.playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)     // visível durante desenvolvimento
                        .setSlowMo(200)         // facilita observar interações
        );

        // (opcional) gravar vídeo por cenário: target/videos/<timestamp>/
        Browser.NewContextOptions ctxOptions = new Browser.NewContextOptions()
                .setViewportSize(1600, 1000)
                //.setRecordVideoDir(Paths.get("target/videos"))  // descomente se quiser vídeo
                //.setRecordVideoSize(1600, 1000)
                ;

        w.context = w.browser.newContext(ctxOptions);
        w.page = w.context.newPage();

        // Default timeout global das operações Playwright (10s costumam ser suficientes):
        w.page.setDefaultTimeout(10_000);
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && w.page != null) {
                // Screenshot on fail
                byte[] bytes = w.page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                scenario.attach(bytes, "image/png", "screenshot-on-failure");

                // (opcional) salvar também em arquivo para inspeção local
                String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                w.page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("target", "screenshots", scenario.getName() + "_" + ts + ".png"))
                        .setFullPage(true));
            }
        } finally {
            if (w.context != null) w.context.close();
            if (w.browser  != null) w.browser.close();
            if (w.playwright != null) w.playwright.close();
        }
    }
}
