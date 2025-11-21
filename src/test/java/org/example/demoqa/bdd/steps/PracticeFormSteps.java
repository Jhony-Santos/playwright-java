package org.example.demoqa.bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.pt.*;
import org.example.demoqa.bdd.World;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.PracticeFormPage;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PracticeFormSteps {

    private final World w;
    private PracticeFormPage form;

    public PracticeFormSteps(World world) {
        this.w = world;
    }

    // ---------- Background / Navegação ----------
    @Given("que estou na página {string}")
    public void queEstouNaPagina(String pagina) {
        if ("Practice Form".equalsIgnoreCase(pagina)) {
            form = new HomePage(w.page)
                    .gotoHome()
                    .openForms()
                    .openPracticeForm();
        } else {
            throw new IllegalArgumentException("Página não suportada no step: " + pagina);
        }
    }

    // ---------- E-mail inválido ----------
    @Quando("eu preencho o email com {string}")
    public void euPreenchoOEmailCom(String email) {
        form.fillEmail(email);
    }

    @E("eu clico em Submit")
    public void euClicoEmSubmit() {
        // Em cenários negativos não queremos esperar o modal ⇒ trySubmit()
        form.trySubmit();
    }

    @Entao("o campo email deve estar {string}")
    public void oCampoEmailDeveEstar(String validityProp) {
        // Ex.: "typeMismatch"
        assertTrue(form.emailValidity(validityProp),
                () -> "Esperava validity." + validityProp + " = true para o e-mail");
    }

    @E("o formulário não deve ser submetido")
    public void oFormularioNaoDeveSerSubmetido() {
        assertFalse(form.formIsSubmitted(), "O formulário não deveria ter sido submetido");
    }

    // ---------- Mobile inválido (Scenario Outline) ----------
    @Quando("eu preencho o mobile com {string}")
    public void euPreenchoOMobileCom(String valor) {
        form.fillMobile(valor);
    }

    @Entao("o campo mobile deve estar {string}")
    public void oCampoMobileDeveEstar(String validityProp) {
        // Ex.: "tooShort" ou "patternMismatch"
        assertTrue(form.mobileValidity(validityProp),
                () -> "Esperava validity." + validityProp + " = true para o mobile");
    }



    // ---------- Happy path ----------
    @Quando("eu preencho os campos obrigatórios corretamente")
    public void euPreenchoOsCamposObrigatoriosCorretamente() {
        form
                .fillFirstName("Ana")
                .fillLastName("Silva")
                .selectGender("Female")
                .fillMobile("9998887766")
                .submit(); // aqui esperamos abrir o modal
    }

    @Entao("o modal de sucesso deve aparecer")
    public void oModalDeSucessoDeveAparecer() {
        assertThat(form.resultModalTitle()).hasText("Thanks for submitting the form");
        form.closeResultModal();
    }
}
