package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.data.AutoCompleteDataFactory;
import org.example.demoqa.data.models.AutoCompleteData;
import org.example.demoqa.pages.AutoCompletePage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class AutoCompleteFlowTest extends BaseTest {

    @Test
    void shouldSelectMultipleAndSingleColors() {
        AutoCompleteData data = AutoCompleteDataFactory.valid();

        AutoCompletePage autoComplete = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openAutoComplete();

        autoComplete
                .selectColors(data)
                .assertMultipleColorsSelected(data.multipleColors())
                .assertSingleColorSelected(data.singleColor());
    }

    @Test
    void shouldRemoveColorFromMultipleField() {
        AutoCompletePage autoComplete = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openAutoComplete();

        autoComplete
                .selectMultipleColor("Blue")
                .assertMultipleColorSelected("Blue")
                .removeMultipleColor("Blue")
                .assertMultipleColorNotSelected("Blue");
    }

    @Test
    void shouldSelectOnlySingleColor() {
        AutoCompleteData data = AutoCompleteDataFactory.singleOnly();

        AutoCompletePage autoComplete = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openAutoComplete();

        autoComplete
                .selectSingleColor(data.singleColor())
                .assertSingleColorSelected(data.singleColor());
    }
}