package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.ElementsPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NavigationSmokeTest extends BaseTest {

    @Test
    void shouldOpenAllMainSectionsFromElementsAccordion() {
        // 1) entra na home
        HomePage home = new HomePage(page).gotoHome();

        // 2) abre Elements (primeiro card)
        ElementsPage elementsPage = home.openElements();
        assertThat(page).hasURL("https://demoqa.com/elements");

        // 3) já na /elements, clica nas seções do accordion, em sequência,
        //    sem voltar pra home
        elementsPage
                .openAccordionSection("Forms")
                .openAccordionSection("Alerts, Frame & Windows")
                .openAccordionSection("Widgets")
                .openAccordionSection("Interactions")
                .openAccordionSection("Book Store Application");
    }
}
