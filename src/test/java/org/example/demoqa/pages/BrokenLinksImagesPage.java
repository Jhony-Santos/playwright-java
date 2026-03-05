package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BrokenLinksImagesPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/broken/?(\\?.*)?$");

    // Âncora de carregamento
    private final Locator heading;

    // Links (robusto: anchor logo após o <p>)
    private final Locator validLink;
    private final Locator brokenLink;

    // Imagens
    private final Locator validImage;
    private final Locator brokenImage;

    public BrokenLinksImagesPage(Page page) {
        super(page);

        this.heading = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Broken Links - Images")
        );

        // normalize-space + case-insensitive (mantive seu approach)
        this.validLink = page.locator(
                "xpath=//p[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='valid link']/following::a[1]"
        );
        this.brokenLink = page.locator(
                "xpath=//p[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='broken link']/following::a[1]"
        );

        this.validImage = page.locator(
                "xpath=//p[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='valid image']/following::img[1]"
        );
        this.brokenImage = page.locator(
                "xpath=//p[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='broken image']/following::img[1]"
        );
    }

    /**
     * ✅ Chame no teste (ou após navegar).
     * Retorna this para permitir fluent:
     *   .openBrokenLinksImages().assertPageLoaded()...
     */
    public BrokenLinksImagesPage assertPageLoaded() {
        safeRemoveObstructions();

        // sincroniza com carga mínima do DOM
        try { page.waitForLoadState(LoadState.DOMCONTENTLOADED); } catch (Exception ignored) {}
        try { page.waitForLoadState(LoadState.NETWORKIDLE); } catch (Exception ignored) {}

        // ancora genérica do app (seu BasePage)
        ensureAppIsUp();

        // URL + heading
        assertThat(page).hasURL(URL_REGEX);
        assertThat(heading).isVisible();

        // garante que os elementos principais existem/estão visíveis
        assertThat(validLink).isVisible();
        assertThat(brokenLink).isVisible();
        assertThat(validImage).isVisible();
        assertThat(brokenImage).isVisible();

        return this;
    }

    // ---------- IMAGENS ----------
    public boolean isValidImageLoaded() {
        return isImageLoaded(validImage);
    }

    public boolean isBrokenImageLoaded() {
        return isImageLoaded(brokenImage);
    }

    private boolean isImageLoaded(Locator img) {
        // Critério clássico: complete && naturalWidth > 0
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
     * Robusto: não fixa texto inteiro (só "Click Here"),
     * e valida href contendo demoqa.com
     */
    public void assertValidLinkVisibleAndLabeled() {
        assertThat(validLink).isVisible();
        assertThat(validLink).containsText("Click Here");
        assertThat(validLink).hasAttribute("href", Pattern.compile(".*demoqa\\.com.*"));
    }

    /**
     * Robusto: href deve apontar para status_codes/500
     */
    public void assertBrokenLinkVisibleAndLabeled() {
        assertThat(brokenLink).isVisible();
        assertThat(brokenLink).containsText("Click Here");
        assertThat(brokenLink).hasAttribute("href", Pattern.compile(".*/status_codes/500.*"));
    }
}