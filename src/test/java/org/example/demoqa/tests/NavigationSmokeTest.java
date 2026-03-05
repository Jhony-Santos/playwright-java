package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NavigationSmokeTest extends BaseTest {

    @Test
    void shouldNavigateAcrossMainSectionsFromHomeCards() {

        HomePage home = new HomePage(page).gotoHome();

        home.openElements();
        assertThat(page).hasURL("**/elements");

        home.gotoHome().openForms();
        assertThat(page).hasURL("**/forms");

        home.gotoHome().openAlertsFrameWindows();
        assertThat(page).hasURL("**/alertsWindows");

        home.gotoHome().openWidgets();
        assertThat(page).hasURL("**/widgets");

        home.gotoHome().openInteractions();
        assertThat(page).hasURL("**/interaction");

        home.gotoHome().openBookStoreApplication();
        assertThat(page).hasURL("**/books");
    }
}