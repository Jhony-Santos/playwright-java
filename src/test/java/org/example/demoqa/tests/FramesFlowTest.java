package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.FramesPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class FramesFlowTest extends BaseTest {

    @Test
    void shouldValidateFrame1Content() {
        FramesPage framesPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openFrames();

        framesPage.assertPageLoaded()
                .assertFrame1Content();
    }

    @Test
    void shouldValidateFrame2Content() {
        FramesPage framesPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openFrames();

        framesPage.assertPageLoaded()
                .assertFrame2Content();
    }

    @Test
    void shouldValidateBothFramesContent() {
        FramesPage framesPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openFrames();

        framesPage.assertPageLoaded()
                .assertFramesHaveUsefulSize()
                .assertFrame1Content()
                .assertFrame2Content();
    }
}