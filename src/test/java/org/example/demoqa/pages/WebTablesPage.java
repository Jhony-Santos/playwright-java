package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WebTablesPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/webtables/?(\\?.*)?$");

    public WebTablesPage(Page page) {
        super(page);
    }

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
        } catch (Exception ignored) {
        }

        assertThat(addButton()).isVisible();
        assertThat(searchBox()).isVisible();

        return this;
    }

    // ---------- Locators básicos ----------
    private Locator addButton() { return page.locator("#addNewRecordButton"); }
    private Locator searchBox() { return page.locator("#searchBox"); }

    // modal
    private Locator modalContent() { return page.locator(".modal-content").first(); }
    private Locator modalTitle() { return modalContent().locator(".modal-title").first(); }

    private Locator firstNameInput() { return modalContent().locator("#firstName"); }
    private Locator lastNameInput() { return modalContent().locator("#lastName"); }
    private Locator emailInput() { return modalContent().locator("#userEmail"); }
    private Locator ageInput() { return modalContent().locator("#age"); }
    private Locator salaryInput() { return modalContent().locator("#salary"); }
    private Locator departmentInput() { return modalContent().locator("#department"); }
    private Locator submitButton() { return modalContent().locator("#submit"); }

    // tabela ATUAL do DemoQA
    private Locator table() { return page.locator(".web-tables-wrapper table").first(); }
    private Locator tableRows() { return page.locator(".web-tables-wrapper table tbody tr"); }

    /**
     * Lê a tabela diretamente do DOM atual.
     * Cada linha retorna uma lista de células.
     */
    @SuppressWarnings("unchecked")
    private List<List<String>> tableData() {
        Object raw = page.evaluate("""
            () => {
              const rows = Array.from(document.querySelectorAll('.web-tables-wrapper table tbody tr'));
              return rows.map(row =>
                Array.from(row.querySelectorAll('td'))
                  .map(cell => (cell.textContent || '').trim())
              );
            }
        """);

        List<List<String>> result = new ArrayList<>();

        if (!(raw instanceof List<?> outer)) {
            return result;
        }

        for (Object rowObj : outer) {
            if (!(rowObj instanceof List<?> inner)) {
                continue;
            }

            List<String> row = new ArrayList<>();
            for (Object cellObj : inner) {
                row.add(cellObj == null ? "" : String.valueOf(cellObj));
            }

            result.add(row);
        }

        return result;
    }

    /**
     * Procura a linha pelo e-mail na 4a coluna.
     * Índice esperado:
     * 0 First Name
     * 1 Last Name
     * 2 Age
     * 3 Email
     * 4 Salary
     * 5 Department
     */
    private int findRowIndexByEmail(String email) {
        List<List<String>> rows = tableData();

        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            if (row.size() <= 3) {
                continue;
            }

            String emailCell = row.get(3).trim();
            if (email.equalsIgnoreCase(emailCell)) {
                return i;
            }
        }

        return -1;
    }

    private String visibleRowsDump() {
        List<List<String>> rows = tableData();

        if (rows.isEmpty()) {
            return "<sem linhas visíveis>";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < rows.size(); i++) {
            sb.append(i)
                    .append(": ")
                    .append(String.join(" | ", rows.get(i)))
                    .append("\n");
        }

        return sb.toString().trim();
    }

    private void diagnosticDump(String prefix) {
        try {
            Path dir = Paths.get("target", "diagnostics");
            Files.createDirectories(dir);

            long ts = Instant.now().toEpochMilli();

            Path png = dir.resolve(prefix + "_" + ts + ".png");
            Path html = dir.resolve(prefix + "_" + ts + ".html");
            Path txt = dir.resolve(prefix + "_" + ts + ".txt");

            page.screenshot(new Page.ScreenshotOptions().setPath(png).setFullPage(true));
            Files.writeString(html, page.content(), StandardCharsets.UTF_8);
            Files.writeString(txt,
                    "URL: " + page.url() + System.lineSeparator() +
                            "TITLE: " + page.title() + System.lineSeparator() +
                            "SEARCH VALUE: " + searchBox().inputValue() + System.lineSeparator() +
                            "VISIBLE ROWS:" + System.lineSeparator() +
                            visibleRowsDump(),
                    StandardCharsets.UTF_8
            );
        } catch (Exception ignored) {
        }
    }

    public Locator rowByEmail(String email) {
        int idx = findRowIndexByEmail(email);
        if (idx >= 0) {
            return tableRows().nth(idx);
        }
        return page.locator(".__row-not-found__");
    }

    public WebTablesPage openAddForm() {
        safeRemoveObstructions();
        addButton().scrollIntoViewIfNeeded();
        addButton().click();

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

        assertThat(firstNameInput()).hasValue(firstName);
        assertThat(lastNameInput()).hasValue(lastName);
        assertThat(emailInput()).hasValue(email);
        assertThat(ageInput()).hasValue(String.valueOf(age));
        assertThat(salaryInput()).hasValue(String.valueOf(salary));
        assertThat(departmentInput()).hasValue(department);

        return this;
    }

    public WebTablesPage submitForm() {
        safeRemoveObstructions();

        submitButton().scrollIntoViewIfNeeded();
        assertThat(submitButton()).isVisible();
        assertThat(submitButton()).isEnabled();

        submitButton().click();

        boolean modalStillVisible;
        try {
            modalContent().waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.HIDDEN)
                    .setTimeout(5_000));
            modalStillVisible = false;
        } catch (Exception ignored) {
            modalStillVisible = modalContent().isVisible();
        }

        if (modalStillVisible) {
            Locator invalidFields = modalContent().locator("input:invalid");
            int invalidCount = invalidFields.count();

            if (invalidCount > 0) {
                throw new AssertionError("O formulário permaneceu aberto após submit. Há "
                        + invalidCount + " campo(s) inválido(s) no modal.");
            }

            submitButton().press("Enter");

            try {
                modalContent().waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
                        .setTimeout(5_000));
            } catch (Exception e) {
                throw new AssertionError("O modal permaneceu aberto após clicar em Submit. "
                        + "O submit não foi concluído ou o clique não foi efetivado.", e);
            }
        }

        assertThat(searchBox()).isVisible();
        assertThat(table()).isVisible();

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
        searchBox().scrollIntoViewIfNeeded();
        searchBox().click();
        searchBox().fill("");
        assertThat(searchBox()).hasValue("");

        searchBox().fill(text);
        assertThat(searchBox()).hasValue(text);

        page.waitForTimeout(700);

        return this;
    }

    public WebTablesPage searchAndWaitByEmail(String email) {
        search(email);

        long deadline = System.currentTimeMillis() + 10_000;

        while (System.currentTimeMillis() < deadline) {
            int idx = findRowIndexByEmail(email);

            if (idx >= 0) {
                return this;
            }

            // se a tabela existir mas não houver linhas, pode ser filtro vazio
            if (tableData().isEmpty()) {
                page.waitForTimeout(250);
                continue;
            }

            page.waitForTimeout(250);
        }

        diagnosticDump("webtables_search_failed");

        throw new AssertionError(
                "A busca foi preenchida com o e-mail, mas a linha não foi localizada: " + email
                        + "\nLinhas visíveis:\n" + visibleRowsDump()
        );
    }

    public WebTablesPage assertRowMatches(
            String email,
            String firstName,
            String lastName,
            int age,
            int salary,
            String department
    ) {
        int idx = findRowIndexByEmail(email);
        if (idx < 0) {
            diagnosticDump("webtables_assert_row_missing");
            throw new AssertionError(
                    "Linha não encontrada para o e-mail: " + email
                            + "\nLinhas visíveis:\n" + visibleRowsDump()
            );
        }

        List<String> row = tableData().get(idx);

        if (row.size() <= 5) {
            diagnosticDump("webtables_row_incomplete");
            throw new AssertionError(
                    "A linha encontrada não possui colunas suficientes. Linha lida: " + row
            );
        }

        assertThat(rowByEmail(email)).isVisible();

        if (!row.get(0).equals(firstName)) {
            throw new AssertionError("First Name divergente. Esperado: " + firstName + " | Atual: " + row.get(0));
        }
        if (!row.get(1).equals(lastName)) {
            throw new AssertionError("Last Name divergente. Esperado: " + lastName + " | Atual: " + row.get(1));
        }
        if (!row.get(2).equals(String.valueOf(age))) {
            throw new AssertionError("Age divergente. Esperado: " + age + " | Atual: " + row.get(2));
        }
        if (!row.get(3).equalsIgnoreCase(email)) {
            throw new AssertionError("Email divergente. Esperado: " + email + " | Atual: " + row.get(3));
        }
        if (!row.get(4).equals(String.valueOf(salary))) {
            throw new AssertionError("Salary divergente. Esperado: " + salary + " | Atual: " + row.get(4));
        }
        if (!row.get(5).equals(department)) {
            throw new AssertionError("Department divergente. Esperado: " + department + " | Atual: " + row.get(5));
        }

        return this;
    }
}