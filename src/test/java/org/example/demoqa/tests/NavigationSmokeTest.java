package org.example.demoqa.tests;


import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;
import static org.example.demoqa.support.TestAsserts.assertOnPath;


public class NavigationSmokeTest extends BaseTest {

    @Test
    void shouldNavigateAcrossMainSectionsFromHomeCards() {

        HomePage home = new HomePage(page).gotoHome();

        home.openElements();
        assertOnPath(page, "/elements");

        home.gotoHome().openForms();
        assertOnPath(page, "/forms");

        home.gotoHome().openAlertsFrameWindows();
        assertOnPath(page, "/alertsWindows");

        home.gotoHome().openWidgets();
        assertOnPath(page, "/widgets");

        home.gotoHome().openInteractions();
        assertOnPath(page, "/interaction");

        home.gotoHome().openBookStoreApplication();
        assertOnPath(page, "/books");


    }

}