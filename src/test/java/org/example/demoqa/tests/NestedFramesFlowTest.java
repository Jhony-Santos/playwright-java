package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.NestedFramesPage;
import org.junit.jupiter.api.Test;

public class NestedFramesFlowTest extends BaseTest {

    @Test
    void shouldValidateParentFrameText() {
        NestedFramesPage nestedFramesPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openNestedFrames();

        nestedFramesPage.assertPageLoaded()
                .assertParentFrameText();
    }

    @Test
    void shouldValidateChildFrameText() {
        NestedFramesPage nestedFramesPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openNestedFrames();

        nestedFramesPage.assertPageLoaded()
                .assertChildFrameText();
    }

    @Test
    void shouldValidateParentAndChildFrames() {
        NestedFramesPage nestedFramesPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openNestedFrames();

        nestedFramesPage.assertPageLoaded()
                .assertParentAndChildFrames();
    }
}