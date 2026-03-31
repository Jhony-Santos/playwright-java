package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.ToolTipsPage;
import org.junit.jupiter.api.Test;

public class ToolTipsFlowTest extends BaseTest {

    @Test
    void shouldShowTooltipOnButton() {
        ToolTipsPage toolTipsPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openToolTips();

        toolTipsPage
                .hoverButton()
                .assertTooltip("You hovered over the Button");
    }

    @Test
    void shouldShowTooltipOnInput() {
        ToolTipsPage toolTipsPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openToolTips();

        toolTipsPage
                .hoverInput()
                .assertTooltip("You hovered over the text field");
    }

    @Test
    void shouldShowTooltipOnLink() {
        ToolTipsPage toolTipsPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openToolTips();

        toolTipsPage
                .hoverContraryLink()
                .assertTooltip("You hovered over the Contrary");
    }
}