package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.ResizablePage;
import org.junit.jupiter.api.Test;

public class ResizableFlowTest extends BaseTest {

    @Test
    void shouldResizeRestrictedBoxWithinLimits() {
        ResizablePage resizablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openResizable();

        resizablePage
                .resizeRestrictedBox(250, 180)
                .assertRestrictedBoxSizeIncreased()
                .assertRestrictedBoxWithinLimits();
    }

    @Test
    void shouldResizeFreeBox() {
        ResizablePage resizablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openResizable();

        resizablePage
                .resizeFreeBox(120, 100)
                .assertFreeBoxSizeIncreased();
    }
}