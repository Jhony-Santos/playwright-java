package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.ProgressBarPage;
import org.junit.jupiter.api.Test;

public class ProgressBarFlowTest extends BaseTest {

    @Test
    void shouldValidateInitialProgressBarState() {
        ProgressBarPage progressBarPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openProgressBar();

        progressBarPage.assertInitialState();
    }

    @Test
    void shouldStartAndStopProgressBar() {
        ProgressBarPage progressBarPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openProgressBar();

        progressBarPage
                .start()
                .waitUntilProgressAtLeast(25)
                .stop()
                .assertProgressBetween(25, 99);
    }

    @Test
    void shouldCompleteAndResetProgressBar() {
        ProgressBarPage progressBarPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openProgressBar();

        progressBarPage
                .start()
                .waitUntilComplete()
                .assertProgressValue(100)
                .reset();
    }
}