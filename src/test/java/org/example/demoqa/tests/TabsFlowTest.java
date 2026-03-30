package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.TabsPage;
import org.junit.jupiter.api.Test;

public class TabsFlowTest extends BaseTest {

    @Test
    void shouldValidateDefaultWhatTab() {
        TabsPage tabsPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openTabs();

        tabsPage
                .assertWhatTabActive()
                .assertWhatContent()
                .assertMoreTabDisabled();
    }

    @Test
    void shouldOpenOriginTab() {
        TabsPage tabsPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openTabs();

        tabsPage
                .clickOriginTab()
                .assertOriginContent()
                .assertMoreTabDisabled();
    }

    @Test
    void shouldOpenUseTab() {
        TabsPage tabsPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openTabs();

        tabsPage
                .clickUseTab()
                .assertUseContent()
                .assertMoreTabDisabled();
    }
}