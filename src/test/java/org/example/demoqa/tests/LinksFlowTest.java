package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.LinksPage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LinksFlowTest extends BaseTest {

    @Test
    void shouldValidateLinksPageLoadedAndBasicElements() {
        LinksPage links = new HomePage(page)
                .gotoHome()
                .openElements()
                .openLinks();

        links.assertPageLoaded();

        // smoke mínimo: garante que existem links principais
        assertThat(links.homeLink()).isVisible();
        assertThat(links.createdLink()).isVisible();
    }
}
