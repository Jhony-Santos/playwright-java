package org.example.demoqa.tests;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NavigationSmokeTest extends BaseTest {

    @Test
    void shouldOpenAllMainSectionsFromHome() {
        HomePage home = new HomePage(page).gotoHome();

        // Elements
        home.openElements();
        assertThat(page).hasURL("**/elements");

        // volta pra home
        home.gotoHome();

        // Forms
        home.openForms();
        assertThat(page).hasURL("**/forms");
        home.gotoHome();

        // Alerts, Frame & Windows
        home.openAlertsFrameWindows();
        assertThat(page).hasURL("**/alertsWindows");
        home.gotoHome();

        // Widgets
        home.openWidgets();
        assertThat(page).hasURL("**/widgets");
        home.gotoHome();

        // Interactions
        home.openInteractions();
        assertThat(page).hasURL("**/interaction");
        home.gotoHome();

        // Book Store Application
        home.openBookStoreApplication();
        assertThat(page).hasURL("**/books");
    }
}
