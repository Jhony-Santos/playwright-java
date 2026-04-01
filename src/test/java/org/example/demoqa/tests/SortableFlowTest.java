package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.SortablePage;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SortableFlowTest extends BaseTest {

    @Test
    void shouldReorderListItems() {
        SortablePage sortablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openSortable();

        sortablePage
                .dragListItemToEnd("One", "Six")
                .assertListOrder(List.of("Two", "Three", "Four", "Five", "Six", "One"));
    }

    @Test
    void shouldReorderGridItems() {
        SortablePage sortablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openSortable();

        sortablePage
                .dragGridItemToEnd("One", "Nine")
                .assertGridOrder(List.of("Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "One"));
    }
}