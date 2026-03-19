package org.example.demoqa.tests;

import com.microsoft.playwright.Locator;
import org.example.demoqa.BaseTest;
import org.example.demoqa.data.WebTableRowDataFactory;
import org.example.demoqa.data.models.WebTableRowData;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.WebTablesPage;
import org.junit.jupiter.api.Test;
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

        webTables.addRecord(
                data.firstName(),
                data.lastName(),
                data.email(),
                data.age(),
                data.salary(),
                data.department()
        );

        webTables.searchAndWaitByEmail(data.email());

        Locator row = webTables.rowByEmail(data.email());
        assertThat(row).isVisible();

        webTables.assertRowMatches(
                data.email(),
                data.firstName(),
                data.lastName(),
                data.age(),
                data.salary(),
                data.department()
        );
    }
}