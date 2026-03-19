package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BrokenLinksImagesPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/broken/?(\\?.*)?$");

    private final Locator heading;

    private final Locator validLink;
    private final Locator brokenLink;

    private final Locator validImage;
    private final Locator brokenImage;

    public BrokenLinksImagesPage(Page page) {
        super(page);

        this.heading = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Broken Links - Images")
        );

        this.validLink = page.locator(
                "xpath=//p[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='valid link']/following-sibling::a[1]"
        );

        this.brokenLink = page.locator(
                "xpath=//p[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='broken link']/following-sibling::a[1]"
        );

        this.validImage = page.locator(
                "xpath=//p[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='valid image']/following-sibling::img[1]"
        );

        this.brokenImage = page.locator(
                "xpath=//p[translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='broken image']/following-sibling::img[1]"
        );
    }

    public BrokenLinksImagesPage assertPageLoaded() {
        safeRemoveObstructions();

        try {
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        } catch (Exception ignored) {}

        ensureAppIsUp();

        assertThat(page).hasURL(URL_REGEX);
        assertThat(heading).isVisible();

        assertThat(validLink).isVisible();
        assertThat(brokenLink).isVisible();
        assertThat(validImage).isVisible();
        assertThat(brokenImage).isVisible();

        waitForImageSettlement(validImage, 10_000);
        waitForImageSettlement(brokenImage, 10_000);

        return this;
    }

    // ---------- IMAGENS ----------

    public boolean isBrokenImageLoaded() {
        return isImageLoaded(brokenImage);
    }

    public String validImageSrc() {
        return validImage.getAttribute("src");
    }

    public String brokenImageSrc() {
        return brokenImage.getAttribute("src");
    }

    private void waitForImageSettlement(Locator img, long timeoutMs) {
        try {
            img.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));

            page.waitForFunction(
                    "(el) => !!el && el.complete === true",
                    img.elementHandle(),
                    new Page.WaitForFunctionOptions().setTimeout(timeoutMs)
            );
        } catch (Exception ignored) {
            // não quebra aqui; a validação final decide
        }
    }

    private boolean isImageLoaded(Locator img) {
        try {
            Object result = img.evaluate("""
                el => {
                  if (!el) return false;
                  const complete = el.complete === true;
                  const naturalWidth = Number(el.naturalWidth || 0);
                  const src = el.currentSrc || el.src || '';
                  return complete && naturalWidth > 0 && src.trim().length > 0;
                }
            """);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }

    // ---------- LINKS ----------

    public String validLinkHref() {
        return validLink.getAttribute("href");
    }

    public String brokenLinkHref() {
        return brokenLink.getAttribute("href");
    }

    public void assertValidLinkVisibleAndLabeled() {
        assertThat(validLink).isVisible();
        assertThat(validLink).containsText("Click Here");
        assertThat(validLink).hasAttribute("href", Pattern.compile(".*demoqa\\.com.*"));
    }

    public void assertBrokenLinkVisibleAndLabeled() {
        assertThat(brokenLink).isVisible();
        assertThat(brokenLink).containsText("Click Here");
        assertThat(brokenLink).hasAttribute("href", Pattern.compile(".*/status_codes/500.*"));
    }
}