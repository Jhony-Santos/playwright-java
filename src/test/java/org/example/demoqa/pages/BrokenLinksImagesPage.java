package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;



public class BrokenLinksImagesPage extends BasePage {

    // Âncora de carregamento (boa prática): Heading (H1) pelo role
    private final Locator heading = page.getByRole(
            AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("Broken Links - Images")
    );

    // LINKS (robusto: no DOM atual NÃO há id; o <a> vem logo após o <p> "Valid Link"/"Broken Link")
    private final Locator validLink = page.locator("xpath=//p[normalize-space()='Valid Link']/following::a[1]");
    private final Locator brokenLink = page.locator("xpath=//p[normalize-space()='Broken Link']/following::a[1]");

    // IMAGENS: pega a 1ª <img> após o texto
    private final Locator validImage = page.locator("xpath=//p[normalize-space()='Valid image']/following::img[1]");
    private final Locator brokenImage = page.locator("xpath=//p[normalize-space()='Broken image']/following::img[1]");

    public BrokenLinksImagesPage(Page page) {
        super(page);
        removeObstructions();

        // BOA PRÁTICA: validar URL + elemento âncora único
        assertLoaded(Pattern.compile(".*/broken$"), heading);
    }

    // ---------- IMAGENS ----------
    public boolean isValidImageLoaded() {
        return isImageLoaded(validImage);
    }

    public boolean isBrokenImageLoaded() {
        return isImageLoaded(brokenImage);
    }

    private boolean isImageLoaded(Locator img) {
        // Critério clássico do front-end: complete && naturalWidth > 0
        Object result = img.evaluate("el => el.complete === true && el.naturalWidth > 0");
        return Boolean.TRUE.equals(result);
    }

    // ---------- LINKS ----------
    public String validLinkHref() {
        return validLink.getAttribute("href");
    }

    public String brokenLinkHref() {
        return brokenLink.getAttribute("href");
    }

    /**
     * Robusto: valida visibilidade + texto + padrão do href
     * (sem depender de ids que podem não existir).
     */
    public void assertValidLinkVisibleAndLabeled() {
        assertThat(validLink).isVisible();
        assertThat(validLink).hasText("Click Here for Valid Link");
        assertThat(validLink).hasAttribute("href", Pattern.compile(".*demoqa\\.com.*"));
    }

    public void assertBrokenLinkVisibleAndLabeled() {
        assertThat(brokenLink).isVisible();
        assertThat(brokenLink).hasText("Click Here for Broken Link");
        assertThat(brokenLink).hasAttribute("href", Pattern.compile(".*/status_codes/500.*"));
    }
}
