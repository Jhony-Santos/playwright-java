package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.LinksPage;
import org.junit.jupiter.api.Test;

public class LinksFlowTest extends BaseTest {

    @Test
    void shouldValidateLinksPageLoadedAndNewTabLinks() {
        LinksPage links = new HomePage(page)
                .gotoHome()
                .openElements()
                .openLinks()
                .assertPageLoaded();

        links
                .clickHomeAndAssertNewTab()
                .clickDynamicHomeAndAssertNewTab();
    }

    @Test
    void shouldValidateAllApiLinksResponses() {
        LinksPage links = new HomePage(page)
                .gotoHome()
                .openElements()
                .openLinks()
                .assertPageLoaded();

        links.clickCreated()
                .clickNoContent()
                .clickMoved()
                .clickBadRequest()
                .clickUnauthorized()
                .clickForbidden()
                .clickNotFound();
    }
}
