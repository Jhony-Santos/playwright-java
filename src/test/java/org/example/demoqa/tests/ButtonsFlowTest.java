package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.ButtonsPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ButtonsFlowTest extends BaseTest {

    @Test
    void shouldValidateButtonsClicks() {
        ButtonsPage buttons = new HomePage(page)
                .gotoHome()
                .openElements()
                .openButtons();

        buttons.assertPageLoaded();

        buttons.doubleClick();
        assertThat(buttons.doubleClickResult()).containsText("You have done a double click");

        buttons.rightClick();
        assertThat(buttons.rightClickResult()).containsText("You have done a right click");

        buttons.clickDynamic();
        assertThat(buttons.dynamicClickResult()).containsText("You have done a dynamic click");
    }
}
