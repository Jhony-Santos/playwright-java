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

        // 1) Validar imagens via DOM
        Assertions.assertTrue(
                brokenPage.isValidImageLoaded(),
                "Imagem válida deveria carregar (naturalWidth > 0)."
        );

        Assertions.assertFalse(
                brokenPage.isBrokenImageLoaded(),
                "Imagem quebrada NÃO deveria carregar (naturalWidth == 0)."
        );

        // 2) Validar links na UI (texto + href) - robusto
        brokenPage.assertValidLinkVisibleAndLabeled();
        brokenPage.assertBrokenLinkVisibleAndLabeled();

        String validUrl = brokenPage.validLinkHref();
        String brokenUrl = brokenPage.brokenLinkHref();

        Assertions.assertNotNull(validUrl, "Href do link válido veio nulo.");
        Assertions.assertNotNull(brokenUrl, "Href do link quebrado veio nulo.");

        // 3) Validar links via HTTP (robusto e determinístico)
        try (HttpStatusClient http = new HttpStatusClient(playwright)) {
            int validStatus = http.getStatus(validUrl);
            int brokenStatus = http.getStatus(brokenUrl);

            // Robusto: link "válido" pode retornar 2xx OU 3xx (redirect)
            Assertions.assertTrue(
                    validStatus >= 200 && validStatus < 400,
                    "Link válido deveria retornar 2xx/3xx (redirect aceito). Status: " + validStatus
            );

            // Link quebrado deve retornar 4xx/5xx
            Assertions.assertTrue(
                    brokenStatus >= 400,
                    "Link quebrado deveria retornar 4xx/5xx. Status: " + brokenStatus
            );
        }
    }






}
