package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

public class LinksPage extends BasePage {

    private static final Pattern URL = Pattern.compile(".*/links$");

    public LinksPage(Page page) {
        super(page);
    }

    // Âncora única: o H1 "Links"
    private Locator titleH1() {
        return page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName("Links")
                        .setLevel(1)
                        .setExact(true)
        );
    }

    public void assertPageLoaded() {
        assertLoaded(URL, titleH1());
    }

    // (Opcional) Se quiser validar também que os links estão renderizados:
    public Locator homeLink() {
        return page.locator("#simpleLink");
    }

    public Locator createdLink() {
        return page.locator("#created");
    }

    public Locator linkResponse() {
        return page.locator("#linkResponse");
    }
}
