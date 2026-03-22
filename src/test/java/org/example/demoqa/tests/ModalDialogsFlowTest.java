package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.ModalDialogsPage;
import org.junit.jupiter.api.Test;

public class ModalDialogsFlowTest extends BaseTest {

    @Test
    void shouldOpenAndCloseSmallModal() {
        ModalDialogsPage modalDialogsPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openModalDialogs();

        modalDialogsPage.assertPageLoaded()
                .openSmallModal()
                .assertSmallModalContent()
                .closeSmallModal();
    }

    @Test
    void shouldOpenAndCloseLargeModal() {
        ModalDialogsPage modalDialogsPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openModalDialogs();

        modalDialogsPage.assertPageLoaded()
                .openLargeModal()
                .assertLargeModalContent()
                .closeLargeModal();
    }
}