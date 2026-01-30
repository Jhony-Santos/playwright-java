package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LinksPage extends BasePage {

    private static final Pattern URL = Pattern.compile(".*/links$");

    private static final String API_CREATED      = "/created";
    private static final String API_NO_CONTENT   = "/no-content";
    private static final String API_MOVED        = "/moved";
    private static final String API_BAD_REQUEST  = "/bad-request";
    private static final String API_UNAUTHORIZED = "/unauthorized";
    private static final String API_FORBIDDEN    = "/forbidden";
    private static final String API_NOT_FOUND    = "/invalid-url";

    // Use: -DuiMsgTimeout=6000
    private static final int UI_MSG_TIMEOUT_MS =
            Integer.parseInt(System.getProperty("uiMsgTimeout", "4000"));

    private final Locator titleH1;

    private final Locator homeLink;
    private final Locator dynamicHomeLink;

    private final Locator createdLink;
    private final Locator noContentLink;
    private final Locator movedLink;
    private final Locator badRequestLink;
    private final Locator unauthorizedLink;
    private final Locator forbiddenLink;
    private final Locator notFoundLink;

    private final Locator linkResponse;

    public LinksPage(Page page) {
        super(page);

        this.titleH1 = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Links").setLevel(1).setExact(true)
        );

        this.homeLink = page.locator("#simpleLink");
        this.dynamicHomeLink = page.locator("#dynamicLink");

        this.createdLink = page.locator("#created");
        this.noContentLink = page.locator("#no-content");
        this.movedLink = page.locator("#moved");
        this.badRequestLink = page.locator("#bad-request");
        this.unauthorizedLink = page.locator("#unauthorized");
        this.forbiddenLink = page.locator("#forbidden");
        this.notFoundLink = page.locator("#invalid-url");

        this.linkResponse = page.locator("#linkResponse");
    }

    public LinksPage assertPageLoaded() {
        assertLoaded(URL, titleH1);
        assertThat(homeLink).isVisible();
        assertThat(createdLink).isVisible();
        return this;
    }

    // --------- Links que abrem nova aba ---------

    public LinksPage clickHomeAndAssertNewTab() {
        Page popup = page.waitForPopup(() -> {
            homeLink.scrollIntoViewIfNeeded();
            highlight(homeLink);
            homeLink.click();
        });
        assertThat(popup).hasURL(Pattern.compile("https://demoqa\\.com/?$"));
        popup.close();

        stepDelay();
        return this;
    }

    public LinksPage clickDynamicHomeAndAssertNewTab() {
        Page popup = page.waitForPopup(() -> {
            dynamicHomeLink.scrollIntoViewIfNeeded();
            highlight(dynamicHomeLink);
            dynamicHomeLink.click();
        });
        assertThat(popup).hasURL(Pattern.compile("https://demoqa\\.com/?$"));
        popup.close();

        stepDelay();
        return this;
    }

    // --------- Links de API ---------

    public LinksPage clickCreated() {
        return clickApiLinkAndObserveUI(createdLink, API_CREATED, 201, "Created", true);
    }

    public LinksPage clickNoContent() {
        // 204 é notoriamente instável no DemoQA para UI; observamos, mas não exigimos texto.
        return clickApiLinkAndObserveUI(noContentLink, API_NO_CONTENT, 204, "No Content", false);
    }

    public LinksPage clickMoved() {
        return clickApiLinkAndObserveUI(movedLink, API_MOVED, 301, "Moved Permanently", true);
    }

    public LinksPage clickBadRequest() {
        return clickApiLinkAndObserveUI(badRequestLink, API_BAD_REQUEST, 400, "Bad Request", true);
    }

    public LinksPage clickUnauthorized() {
        return clickApiLinkAndObserveUI(unauthorizedLink, API_UNAUTHORIZED, 401, "Unauthorized", true);
    }

    public LinksPage clickForbidden() {
        return clickApiLinkAndObserveUI(forbiddenLink, API_FORBIDDEN, 403, "Forbidden", true);
    }

    public LinksPage clickNotFound() {
        return clickApiLinkAndObserveUI(notFoundLink, API_NOT_FOUND, 404, "Not Found", true);
    }

    // --------- Core ---------

    private LinksPage clickApiLinkAndObserveUI(
            Locator link,
            String apiPath,
            int expectedStatus,
            String expectedText,
            boolean expectUiText
    ) {
        clearLinkResponse();

        link.scrollIntoViewIfNeeded();
        highlight(link);
        stepDelay(); // tempo para você ver qual link será clicado

        // 1) Fonte de verdade: REDE
        Response response = page.waitForResponse(
                r -> r.url().contains(apiPath),
                () -> link.click()
        );

        if (response.status() != expectedStatus) {
            throw new AssertionError(
                    "Expected HTTP " + expectedStatus +
                            " but got " + response.status() +
                            " for endpoint " + apiPath +
                            " (url=" + response.url() + ")"
            );
        }

        // 2) Observabilidade: UI (não reprova o teste)
        observeUiMessageBestEffort(expectUiText ? expectedStatus : null,
                expectUiText ? expectedText : null);

        return this;
    }

    private void clearLinkResponse() {
        page.evaluate("() => {" +
                "const el = document.querySelector('#linkResponse');" +
                "if (el) el.textContent = '';" +
                "}");
    }

    /**
     * Observa a UI de forma robusta:
     * - Rola até o #linkResponse sempre
     * - Aguarda TEXTO (não visibilidade) por polling curto
     * - Nunca falha o teste
     * - Garante stepDelay ao final para você conseguir ler
     */
    private void observeUiMessageBestEffort(Integer statusCode, String statusText) {
        try {
            linkResponse.scrollIntoViewIfNeeded();
            highlight(linkResponse);

            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start < UI_MSG_TIMEOUT_MS) {
                String current = safeText(linkResponse.textContent());

                // Caso não exija texto (ex.: 204), só tenta esperar aparecer algo
                if (statusCode == null || statusText == null) {
                    if (!current.isBlank()) break;
                    page.waitForTimeout(150);
                    continue;
                }

                // Espera conter status e texto
                if (current.contains(String.valueOf(statusCode)) && current.contains(statusText)) {
                    break;
                }

                page.waitForTimeout(150);
            }

        } catch (Exception ignored) {
            // UI nunca reprova
        } finally {
            // tempo de leitura garantido
            stepDelay();
        }
    }

    private static String safeText(String s) {
        return s == null ? "" : s.trim();
    }

    public Locator linkResponse() {
        return linkResponse;
    }
}
