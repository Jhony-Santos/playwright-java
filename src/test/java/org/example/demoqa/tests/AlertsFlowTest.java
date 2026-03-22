package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.AlertsPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class AlertsFlowTest extends BaseTest {

    @Test
    void shouldOpenSimpleAlertAndAccept() {
        AlertsPage alertsPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openAlerts();

        alertsPage.assertPageLoaded()
                .openSimpleAlertAndAccept();
    }

    @Test
    void shouldOpenTimerAlertAndAccept() {
        AlertsPage alertsPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openAlerts();

        alertsPage.assertPageLoaded()
                .openTimerAlertAndAccept();
    }

    @Test
    void shouldAcceptConfirmAndValidateResult() {
        AlertsPage alertsPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openAlerts();

        alertsPage.assertPageLoaded()
                .openConfirmAndAccept()
                .assertConfirmAccepted();
    }

    @Test
    void shouldFillPromptAndValidateResult() {
        AlertsPage alertsPage = new HomePage(page)
                .gotoHome()
                .openAlertsFrameWindows()
                .openAlerts();

        alertsPage.assertPageLoaded()
                .openPromptAndAccept("Jhow")
                .assertPromptResult("Jhow");
    }
}