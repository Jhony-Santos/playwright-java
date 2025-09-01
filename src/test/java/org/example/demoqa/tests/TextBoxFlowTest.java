package org.example.demoqa.tests;
import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class TextBoxFlowTest extends BaseTest {
    @Test
    void fluxo_TextBox() {
        new HomePage(page)
                .gotoHome()
                .openElements()
                .openTextBox()
                .fillForm("Jhonatan Santos", "jhony.jpn@gmail.com", "Rua das Flores, 123", "Curitiba - PR")
                .submit()
                .assertOutputContains("Jhonatan Santos", "jhony.jpn@gmail.com");
    }
}
