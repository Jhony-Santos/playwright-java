package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.RadioButtonPage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RadioButtonFlowTest extends BaseTest {

    @Test
    void shouldSelectYesAndImpressive_andNoMustBeDisabled() {
        RadioButtonPage radio = new HomePage(page)
                .gotoHome()
                .openElements()
                .openRadioButton();

        // ainda podemos garantir que estamos na URL certa, mas com pattern
        radio.assertPageLoaded();


        // YES
        radio.selectYes();
        assertTrue(radio.isResultYes());
        assertEquals("You have selected Yes", radio.resultMessage());

        // IMPRESSIVE (sobrescreve o resultado)
        radio.selectImpressive();
        assertTrue(radio.isResultImpressive());
        assertEquals("You have selected Impressive", radio.resultMessage());

        // NO deve estar desabilitado
        assertTrue(radio.isNoDisabled());
    }
}
