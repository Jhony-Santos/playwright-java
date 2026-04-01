package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.SelectablePage;
import org.junit.jupiter.api.Test;

public class SelectableFlowTest extends BaseTest {

    @Test
    void shouldSelectItemInListTab() {
        SelectablePage selectablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openSelectable();

        selectablePage
                .selectListItem("Cras justo odio")
                .assertListItemSelected("Cras justo odio");
    }

    @Test
    void shouldSelectItemInGridTab() {
        SelectablePage selectablePage = new HomePage(page)
                .gotoHome()
                .openInteractions()
                .openSelectable();

        selectablePage
                .selectGridItem("Two")
                .assertGridItemSelected("Two");
    }
}