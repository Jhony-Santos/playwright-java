package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.MenuPage;
import org.junit.jupiter.api.Test;

public class MenuFlowTest extends BaseTest {

    @Test
    void shouldDisplaySubMenuWhenHoverMainItem2() {
        MenuPage menuPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openMenu();

        menuPage
                .hoverMainItem2()
                .assertSubMenuVisible();
    }

    @Test
    void shouldDisplaySubSubMenuWhenHoverSubSubList() {
        MenuPage menuPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openMenu();

        menuPage
                .hoverMainItem2()
                .assertSubMenuVisible()
                .hoverSubSubList()
                .assertSubSubMenuVisible();
    }
}