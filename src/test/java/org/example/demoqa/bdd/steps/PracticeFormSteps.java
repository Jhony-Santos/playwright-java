package org.example.demoqa.bdd.steps;

import io.cucumber.java.en.*;
import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.PracticeFormPage;

import static org.junit.jupiter.api.Assertions.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PracticeFormSteps extends BaseTest {
    private PracticeFormPage form;

    @Given("que estou na página {string}")
    public void queEstouNaPagina(String name) {
        // navega até o Practice Form pela Home
        form = new HomePage(page).gotoHome().openForms().openPracticeForm();
    }

    @When("eu preencho o email com {string}")
    public void euPreenchoEmail(String email) {
        form.fillEmail(email);
    }

    @When("eu preencho o mobile com {string}")
    public void euPreenchoMobile(String mobile) {
        form.fillMobile(mobile);
    }

    @When("eu clico em Submit")
    public void euClicoEmSubmit() {
        form.trySubmit(); // não espera modal (bom p/ negativos)
    }

    @Then("o campo email deve estar {string}")
    public void campoEmailDeveEstar(String validity) {
        assertTrue(form.emailValidity(validity));
    }

    @Then("o campo mobile deve estar {string}")
    public void campoMobileDeveEstar(String validity) {
        assertTrue(form.mobileValidity(validity));
    }

    @Then("o formulário não deve ser submetido")
    public void formularioNaoDeveSerSubmetido() {
        assertFalse(form.formIsSubmitted());
    }

    @When("eu preencho os campos obrigatórios corretamente")
    public void preenchoObrigatorios() {
        form.fillFirstName("Ana")
                .fillLastName("Silva")
                .selectGender("Female")
                .fillMobile("9998887766")
                .submit();
    }

    @Then("o modal de sucesso deve aparecer")
    public void modalSucessoAparece() {
        assertThat(form.resultModalTitle()).hasText("Thanks for submitting the form");
        form.closeResultModal();
    }
}
