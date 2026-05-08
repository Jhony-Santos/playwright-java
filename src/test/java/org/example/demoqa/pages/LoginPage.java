package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page);
    }

    public LoginPage assertLoaded() {
        ensureAppIsUp(List.of("body", "#userName", "#password", "#login"), 60_000, true);
        assertThat(page.locator("#userName")).isVisible();
        assertThat(page.locator("#password")).isVisible();
        assertThat(page.locator("#login")).isVisible();
        return this;
    }

    public LoginPage login(String user, String pass) {
        assertLoaded();

        page.locator("#userName").fill(user);
        page.locator("#password").fill(pass);

        click(page.locator("#login"));

        Locator loggedUser = page.locator("#userName-value");
        Locator invalidMessage = page.locator("#name");

        try {
            loggedUser.waitFor(new Locator.WaitForOptions().setTimeout(15_000));
        } catch (Exception e) {
            String errorText = invalidMessage.count() > 0 ? invalidMessage.textContent().trim() : "<sem mensagem>";
            throw new AssertionError(
                    "Login não foi concluído. Verifique usuário/senha. Mensagem exibida: " + errorText,
                    e
            );
        }

        assertThat(loggedUser).containsText(user);

        return this;
    }
}