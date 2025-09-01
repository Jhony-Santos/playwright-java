package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class CheckBoxFlowTest extends BaseTest {

    @Test
    void checkBox_selectDesktop() {
        new HomePage(page)
                .gotoHome()
                .openElements()
                .openCheckBox()
                .expandAll()
                .selectByNode("desktop")
                .assertResultContains("desktop");
    }
}
