package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class WebTablesPage extends BasePage {

    //private static final String URL_PATTERN = "**/webtables";
    private static final Pattern URL_REGEX = Pattern.compile(".*/webtables/?(\\?.*)?$");

    public WebTablesPage(Page page) {
        super(page);
    }

    // ---- Asserções de página ----

    public void assertPageLoaded() {
        assertThat(page).hasURL(URL_REGEX);
    }

    // ---- Locators básicos ----

    private Locator addButton() {
        return page.locator("#addNewRecordButton");
    }

    private Locator searchBox() {
        return page.locator("#searchBox");
    }

    // formulário do modal "Registration Form"
    private Locator firstNameInput() { return page.locator("#firstName"); }
    private Locator lastNameInput()  { return page.locator("#lastName"); }
    private Locator emailInput()     { return page.locator("#userEmail"); }
    private Locator ageInput()       { return page.locator("#age"); }
    private Locator salaryInput()    { return page.locator("#salary"); }
    private Locator departmentInput(){ return page.locator("#department"); }
    private Locator submitButton()   { return page.locator("#submit"); }

    // corpo da tabela
    private Locator tableBody() {
        return page.locator(".rt-tbody");
    }

    /**
     * Retorna a linha da tabela que contém o e-mail informado.
     * (cada linha é um .rt-tr-group)
     */
    public Locator rowByEmail(String email) {
        return tableBody().locator(".rt-tr-group")
                .filter(new Locator.FilterOptions().setHasText(email));
    }

    // ---- Ações de mais alto nível ----

    public WebTablesPage openAddForm() {
        addButton().click();
        // o Playwright já vai aguardar o formulário aparecer
        return this;
    }

    public WebTablesPage fillForm(
            String firstName,
            String lastName,
            String email,
            int age,
            int salary,
            String department
    ) {
        firstNameInput().fill(firstName);
        lastNameInput().fill(lastName);
        emailInput().fill(email);
        ageInput().fill(String.valueOf(age));
        salaryInput().fill(String.valueOf(salary));
        departmentInput().fill(department);
        return this;
    }

    public WebTablesPage submitForm() {
        submitButton().click();
        return this;
    }

    /** fluxo completo para cadastrar um registro */
    public WebTablesPage addRecord(
            String firstName,
            String lastName,
            String email,
            int age,
            int salary,
            String department
    ) {
        return openAddForm()
                .fillForm(firstName, lastName, email, age, salary, department)
                .submitForm();
    }

    public WebTablesPage search(String text) {
        searchBox().fill(""); // limpa
        searchBox().fill(text);
        return this;
    }
}
