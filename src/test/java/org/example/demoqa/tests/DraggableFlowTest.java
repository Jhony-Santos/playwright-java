package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.DraggablePage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class DraggableFlowTest extends BaseTest {


    @Test
    void shouldDragSimpleBox() {
        DraggablePage draggablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openDraggable();

        draggablePage
                .dragSimpleBoxBy(180, 80)
                .assertSimpleBoxMovedFromInitialPosition();
    }

    @Test
    void shouldRespectAxisRestriction() {
        DraggablePage draggablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openDraggable();

        draggablePage
                .dragXAxisBox(200, 100)
                .assertMovedOnlyHorizontally();

        draggablePage
                .dragYAxisBox(200, 100)
                .assertMovedOnlyVertically();
    }


    @Test
    void shouldRespectContainerRestriction() {
        DraggablePage draggablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openDraggable();

        draggablePage
                .dragContainedBox(300, 100)
                .assertContainedBoxMovedInsideContainer();

        draggablePage
                .dragContainedParent(40, 40)
                .assertContainedParentMovedInsideParent();
    }


    @Test
    void shouldRespectCursorStyle() {
        DraggablePage draggablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openDraggable();

        draggablePage
                .dragCursorCenterBox(80, 80)
                .dragCursorTopLeftBox(80, 80)
                .dragCursorBottomBox(80, 80);
    }

}