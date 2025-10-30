package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.TextBoxPage;
import org.example.demoqa.data.TextBoxDataFactory;
import org.example.demoqa.data.models.TextBoxData;

import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TextBoxFlowTest extends BaseTest {

    @Test
    void shouldSubmitTextBoxWithValidData() {
        TextBoxData data = TextBoxDataFactory.valid();

        TextBoxPage textBox = new HomePage(page)
                .gotoHome()
                .openElements()
                .openTextBox();

        textBox.fillForm(data).submit();

        assertThat(textBox.outputBox()).isVisible();
        assertThat(textBox.outputBox()).containsText(data.name());
        assertThat(textBox.outputBox()).containsText(data.email());
    }

    @Test
    void shouldSubmitTextBoxWithAnotherValidData() {
        TextBoxData data = TextBoxDataFactory.anotherValid();

        TextBoxPage textBox = new HomePage(page)
                .gotoHome()
                .openElements()
                .openTextBox();

        textBox.fillForm(data).submit();

        assertThat(textBox.outputBox()).containsText(data.name());
        assertThat(textBox.outputBox()).containsText(data.email());
    }
}
