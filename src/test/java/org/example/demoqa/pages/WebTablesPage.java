package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WebTablesPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/webtables/?(\\?.*)?$");

    public WebTablesPage(Page page) {
        super(page);
    }

    // ---- Asserções de página ----
    public WebTablesPage assertPageLoaded() {
        safeRemoveObstructions();

        ensureAppIsUp(List.of("body", "#addNewRecordButton", "#searchBox"), 60_000, true);

        assertThat(page).hasURL(URL_REGEX);

        try {
            Locator header = page.locator("div.main-header, h1").first();
            header.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5_000));
            assertThat(header).containsText("Web Tables");
        } catch (Exception ignored) {}

        assertThat(addButton()).isVisible();
        assertThat(searchBox()).isVisible();

        return this;
    }

    // ---- Locators básicos ----
    private Locator addButton() { return page.locator("#addNewRecordButton"); }
    private Locator searchBox() { return page.locator("#searchBox"); }

    // Modal / form
    private Locator modalContent() { return page.locator(".modal-content").first(); }
    private Locator modalTitle() { return page.locator(".modal-title").first(); }

    private Locator firstNameInput() { return page.locator("#firstName"); }
    private Locator lastNameInput()  { return page.locator("#lastName"); }
    private Locator emailInput()     { return page.locator("#userEmail"); }
    private Locator ageInput()       { return page.locator("#age"); }
    private Locator salaryInput()    { return page.locator("#salary"); }
    private Locator departmentInput(){ return page.locator("#department"); }
    private Locator submitButton()   { return page.locator("#submit"); }

    private Locator tableBody() { return page.locator(".rt-tbody").first(); }
    private Locator noRowsFound() { return page.locator(".rt-noData").first(); }

    /** Retorna a primeira linha da tabela que contém o e-mail informado. */
    public Locator rowByEmail(String email) {
        return tableBody().locator(".rt-tr-group")
                .filter(new Locator.FilterOptions().setHasText(email))
                .first();
    }

    // ---- Ações de alto nível ----
    public WebTablesPage openAddForm() {
        safeRemoveObstructions();
        click(addButton());

        modalContent().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(30_000));

        firstNameInput().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(30_000));

        if (modalTitle().count() > 0) {
            assertThat(modalTitle()).containsText("Registration Form");
        }

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
        safeRemoveObstructions();
        click(submitButton());

        try {
            modalContent().waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.HIDDEN)
                    .setTimeout(10_000));
        } catch (Exception ignored) {
            modalContent().waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.DETACHED)
                    .setTimeout(10_000));
        }

        // espera o grid voltar ao estado interativo
        searchBox().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        return this;
    }

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
        searchBox().fill("");
        searchBox().fill(text);

        waitGridToReflectSearch(text, 10_000);

        return this;
    }

    private void waitGridToReflectSearch(String text, long timeoutMs) {
        try {
            page.waitForFunction(
                    "({ email }) => {" +
                            "  const body = document.querySelector('.rt-tbody');" +
                            "  const noData = document.querySelector('.rt-noData');" +
                            "  const bodyText = (body?.innerText || '').toLowerCase();" +
                            "  const noDataText = (noData?.innerText || '').toLowerCase();" +
                            "  return bodyText.includes(email.toLowerCase()) || noDataText.includes('no rows found');" +
                            "}",
                    Map.of("email", text),
                    new Page.WaitForFunctionOptions().setTimeout(timeoutMs)
            );
        } catch (Exception ignored) {
            // fallback leve: deixa o assert final do teste apontar a falha real
            page.waitForTimeout(1000);
        }
    }
}