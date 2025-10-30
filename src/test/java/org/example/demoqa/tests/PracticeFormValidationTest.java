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

        assertTrue(form.isRequiredFirstName());
        assertTrue(form.firstNameValidity("valueMissing"));
        assertTrue(form.isRequiredLastName());
        assertTrue(form.lastNameValidity("valueMissing"));
        assertTrue(form.isRequiredGender());
        assertTrue(form.genderValidity("valueMissing"));
        assertTrue(form.isRequiredMobile());
        assertTrue(form.mobileValidity("valueMissing"));

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

        assertTrue(form.mobileValidity("tooShort"));
        assertFalse(form.mobileValidationMessage().isBlank());
    }

    @Test
    void mobile_withLetters_shouldPatternMismatch() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        form.fillMobile("abc1234567").trySubmit();

        assertTrue(form.mobileValidity("patternMismatch"));
        assertFalse(form.mobileValidationMessage().isBlank());
    }

    @Test
    void email_withInvalidFormat_shouldTypeMismatch() {
        PracticeFormPage form = new HomePage(page).
                gotoHome().
                openForms().
                openPracticeForm();

        form.fillFirstName("Jhon Hacker")
                .fillLastName("Silva")
                .selectGender("Female")
                .fillMobile("9998887766");

        form.fillEmail("foo");

        form.trySubmit();

        // Agora o browser valida o e-mail e marca typeMismatch = true
        form.emailValidity("typeMismatch");
        assertFalse(form.formIsSubmitted());


    }

    @Test
    void mobile_withElevenDigits_shouldBeTruncatedToTen() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        // envia 11 dígitos
        form.fillMobile("12345678901");

        // lê o valor que ficou no input
        String value = form.mobileValue();

        // o input da página tem maxlength=10 -> deve truncar
        assertEquals("1234567890", value);
    }

    @Test
    void happyPath_minimalRequired_shouldOpenModal() {
        PracticeFormPage form = new HomePage(page).gotoHome().openForms().openPracticeForm();

        form.fillFirstName("Ana")
                .fillLastName("Silva")
                .selectGender("Female")
                .fillMobile("9998887766")
                .submit();

        assertThat(form.resultModalTitle()).hasText("Thanks for submitting the form");
        form.closeResultModal();
    }





}
