package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.DynamicPropertiesPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicPropertiesTest extends BaseTest {

    @Test
    void shouldValidateDynamicProperties() {
        DynamicPropertiesPage pageDyn = new HomePage(page)
                .gotoHome()
                .openElementsByClick()
                .openDynamicProperties();

        pageDyn.assertLoaded();

        // ✅ CAPTURA ESTADOS INICIAIS IMEDIATAMENTE (antes de aguardar 5s)
        pageDyn.assertEnableButtonDisabledInitially();
        String beforeClass = pageDyn.colorButtonClass();
        assertNotNull(beforeClass, "Class inicial do botão Color Change veio nula.");
        pageDyn.assertVisibleAfterHiddenInitially();

        // 1) Enable after ~5s
        pageDyn.waitEnableButtonEnabled();

        // 2) Color change after ~5s (validar por mudança REAL de class)
        pageDyn.waitColorButtonClassToChange(beforeClass);
        String afterClass = pageDyn.colorButtonClass();
        assertNotNull(afterClass, "Class final do botão Color Change veio nula.");
        assertNotEquals(beforeClass, afterClass, "A classe do botão deveria mudar após ~5s.");

        // (Opcional) se quiser reforçar:
        // pageDyn.waitColorButtonHasSuccessClass();

        // 3) Visible after ~5s
        pageDyn.waitVisibleAfterVisible();
    }
}
