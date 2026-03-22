package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.AccordionPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class AccordionFlowTest extends BaseTest {

    @Test
    void shouldValidateFirstSectionIsOpenByDefault() {
        AccordionPage accordionPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openAccordion();

        accordionPage.assertPageLoaded()
                .assertFirstSectionOpenByDefault();
    }

    @Test
    void shouldOpenSecondSectionAndValidateContent() {
        AccordionPage accordionPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openAccordion();

        accordionPage.assertPageLoaded()
                .openSecondSection()
                .assertSecondSectionContent();
    }

    @Test
    void shouldOpenThirdSectionAndValidateContent() {
        AccordionPage accordionPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openAccordion();

        accordionPage.assertPageLoaded()
                .openThirdSection()
                .assertThirdSectionContent();
    }
}