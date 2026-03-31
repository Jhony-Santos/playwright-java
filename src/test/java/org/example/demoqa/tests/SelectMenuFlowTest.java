package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.data.SelectMenuDataFactory;
import org.example.demoqa.data.models.SelectMenuData;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.SelectMenuPage;
import org.junit.jupiter.api.Test;

public class SelectMenuFlowTest extends BaseTest {

    @Test
    void shouldSelectValuesInAllSelectMenus() {
        SelectMenuData data = SelectMenuDataFactory.valid();

        SelectMenuPage selectMenuPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openSelectMenu();

        selectMenuPage
                .selectValue(data.selectValue())
                .selectOne(data.selectOne())
                .selectOldStyleColor(data.oldStyleColor())
                .selectMultiSelectDropDown(data.multiSelectDropDown())
                .selectStandardMulti(data.standardMultiSelect())
                .assertSelectValueSelected(data.selectValue())
                .assertSelectOneSelected(data.selectOne())
                .assertOldStyleColorSelected(data.oldStyleColor())
                .assertMultiSelectDropDownSelected(data.multiSelectDropDown())
                .assertStandardMultiSelected(data.standardMultiSelect());
    }
}