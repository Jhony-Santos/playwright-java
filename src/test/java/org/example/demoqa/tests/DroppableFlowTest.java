package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.DroppablePage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class DroppableFlowTest extends BaseTest {

    @Test
    void shouldDropOnSimpleTab() {
        DroppablePage droppablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openDroppable();

        droppablePage
                .dragSimpleToDrop()
                .assertSimpleDropped();
    }

    @Test
    void shouldAcceptOnlyAcceptableElement() {
        DroppablePage droppablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openDroppable();

        droppablePage
                .dragAcceptableToDrop()
                .assertAcceptableDropped();
    }

    @Test
    void shouldRejectNotAcceptableElement() {
        DroppablePage droppablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openDroppable();

        droppablePage
                .dragNotAcceptableToDrop()
                .assertNotAcceptableDidNotDrop();
    }

    @Test
    void shouldKeepNotRevertableDropped() {
        DroppablePage droppablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openDroppable();

        droppablePage
                .dragNotRevertableToDrop()
                .assertNotRevertableStayedDropped();
    }
}