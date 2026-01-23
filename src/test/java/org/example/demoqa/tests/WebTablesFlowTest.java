package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.data.WebTableRowDataFactory;
import org.example.demoqa.data.models.WebTableRowData;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.WebTablesPage;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Locator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WebTablesFlowTest extends BaseTest {

    @Test
    void shouldCreateNewRecordAndFindItByEmail() {
        WebTableRowData data = WebTableRowDataFactory.valid();

        WebTablesPage webTables = new HomePage(page)
                .gotoHome()
                .openElements()
                .openWebTables();

        webTables.assertPageLoaded();

        // adiciona novo registro
        webTables.addRecord(
                data.firstName(),
                data.lastName(),
                data.email(),
                data.age(),
                data.salary(),
                data.department()
        );

        // usa o próprio campo de busca da página para filtrar pelo e-mail
        webTables.search(data.email());

        Locator row = webTables.rowByEmail(data.email());

        // asserções em cima da linha retornada
        assertThat(row).isVisible();
        assertThat(row).containsText(data.firstName());
        assertThat(row).containsText(data.lastName());
        assertThat(row).containsText(String.valueOf(data.age()));
        assertThat(row).containsText(String.valueOf(data.salary()));
        assertThat(row).containsText(data.department());
    }
}
