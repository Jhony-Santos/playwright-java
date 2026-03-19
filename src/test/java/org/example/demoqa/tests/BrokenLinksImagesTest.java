package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.clients.HttpStatusClient;
import org.example.demoqa.pages.BrokenLinksImagesPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BrokenLinksImagesTest extends BaseTest {

    @Test
    void shouldValidateBrokenImagesAndLinks() {
        BrokenLinksImagesPage brokenPage = new HomePage(page)
                .gotoHome()
                .openElementsByClick()
                .openBrokenLinksImages()
                .assertPageLoaded();

        // 1) Validar imagem quebrada via DOM (determinístico)
        Assertions.assertFalse(
                brokenPage.isBrokenImageLoaded(),
                "Imagem quebrada NÃO deveria carregar (naturalWidth == 0)."
        );

        // 2) Validar links na UI
        brokenPage.assertValidLinkVisibleAndLabeled();
        brokenPage.assertBrokenLinkVisibleAndLabeled();

        // 3) Extrair URLs/src
        String validUrl = brokenPage.validLinkHref();
        String brokenUrl = brokenPage.brokenLinkHref();
        String validImageSrc = brokenPage.validImageSrc();

        Assertions.assertNotNull(validUrl, "Href do link válido veio nulo.");
        Assertions.assertNotNull(brokenUrl, "Href do link quebrado veio nulo.");
        Assertions.assertNotNull(validImageSrc, "Src da imagem válida veio nulo.");

        // 4) Validar via HTTP (robusto e determinístico)
        try (HttpStatusClient http = new HttpStatusClient(playwright)) {
            int validStatus = http.getStatus(validUrl);
            int brokenStatus = http.getStatus(brokenUrl);
            int imageStatus = http.getStatus(validImageSrc);

            Assertions.assertTrue(
                    validStatus >= 200 && validStatus < 400,
                    "Link válido deveria retornar 2xx/3xx (redirect aceito). Status: " + validStatus
            );

            Assertions.assertTrue(
                    brokenStatus >= 400,
                    "Link quebrado deveria retornar 4xx/5xx. Status: " + brokenStatus
            );

            Assertions.assertTrue(
                    imageStatus >= 200 && imageStatus < 400,
                    "Imagem válida deveria retornar 2xx/3xx. Status: " + imageStatus
            );
        }
    }
}