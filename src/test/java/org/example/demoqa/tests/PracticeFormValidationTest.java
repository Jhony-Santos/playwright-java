package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.PracticeFormPage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PracticeFormValidationTest extends BaseTest {

    @Test
    void requiredFields_whenEmpty_shouldBlockSubmit() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm()
                .trySubmit();

        // ✅ comportamento real: não abre modal
        assertFalse(form.formIsSubmitted());

        // ✅ inputs realmente inválidos
        assertTrue(form.isFirstNameInvalid());
        assertTrue(form.isLastNameInvalid());
        assertTrue(form.isMobileInvalid());

        // ✅ gender não selecionado (validação HTML5 do radio não é confiável no DemoQA)
        assertFalse(form.isAnyGenderSelected());

        // mensagens nativas existem (sem fixar texto/idioma)
        assertFalse(form.firstNameValidationMessage().isBlank());
        assertFalse(form.lastNameValidationMessage().isBlank());
    }

    @Test
    void mobile_withNineDigits_shouldBeTooShort() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        form.fillMobile("123456789").trySubmit();

        // maxlength/regex do DemoQA costuma invalidar via :invalid
        assertTrue(form.isMobileInvalid());
        assertFalse(form.mobileValidationMessage().isBlank());
    }

    @Test
    void mobile_withLetters_shouldPatternMismatch() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        form.fillMobile("abc1234567").trySubmit();

        assertTrue(form.isMobileInvalid());
        assertFalse(form.mobileValidationMessage().isBlank());
    }

    @Test
    void email_withInvalidFormat_shouldTypeMismatch() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        form.fillFirstName("Jhon Hacker")
                .fillLastName("Silva")
                .selectGender("Female")
                .fillMobile("9998887766");

        form.fillEmail("foo");
        form.trySubmit();

        assertTrue(form.emailValidity("typeMismatch"));
        assertFalse(form.formIsSubmitted());
    }

    @Test
    void mobile_withElevenDigits_shouldBeTruncatedToTen() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        form.fillMobile("12345678901");

        String value = form.mobileValue();
        assertEquals("1234567890", value);
    }

    @Test
    void happyPath_minimalRequired_shouldOpenModal() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        form.fillFirstName("Ana")
                .fillLastName("Silva")
                .selectGender("Female")
                .fillMobile("9998887766")
                .submit();

        assertThat(form.resultModalTitle()).hasText("Thanks for submitting the form");
        form.closeResultModal();
    }
}