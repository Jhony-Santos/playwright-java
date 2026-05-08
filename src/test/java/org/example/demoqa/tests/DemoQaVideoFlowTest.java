package org.example.demoqa.tests;

import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Locator;
import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class DemoQaVideoFlowTest extends BaseTest {

    @Test
    void shouldExecuteLinkedinDemoVideoFlow() {
        HomePage home = new HomePage(page);

        home.gotoHome();
        pause();

        // ELEMENTS
        home.openElements();
        pause();

        // TEXT BOX - interação real
        home.openElements().openTextBox();
        runTextBoxScenario();
        pause();

        // CHECK BOX - interação real
        home.openElements().openCheckBox()
                .expandAll()
                .selectByNode("Desktop")
                .assertResultContains("Desktop");
        pause();

        // FORMS
        home.gotoHome();
        home.openForms();
        pause();

        // PRACTICE FORM - interação real
        home.openForms().openPracticeForm();
        runPracticeFormScenario();
        pause();

        // ALERTS
        home.gotoHome();
        home.openAlertsFrameWindows();
        pause();

        // ALERT - interação real
        home.openAlertsFrameWindows().openAlerts();
        runAlertsScenario();
        pause();

        // WIDGETS
        home.gotoHome();
        home.openWidgets();
        pause();

        // ACCORDION - interação real
        home.openWidgets().openAccordion()
                .assertPageLoaded()
                .assertFirstSectionOpenByDefault()
                .openSecondSection()
                .openThirdSection();
        pause();

        // INTERACTIONS
        home.gotoHome();
        home.openInteractions();
        pause();

        // BOOK STORE
        home.gotoHome();
        home.openBookStoreApplication();
        pause();
    }

    private void runTextBoxScenario() {
        typeSlow("#userName", "Jhonatan Santos");
        typeSlow("#userEmail", "jhony.jpn@gmail.com");
        typeSlow("#currentAddress", "Rua das Flores, 123 - Curitiba/PR");
        typeSlow("#permanentAddress", "Curitiba - PR");

        clickAndPause(page.locator("#submit"));
    }

    private void runPracticeFormScenario() {
        typeSlow("#firstName", "Jhonatan");
        typeSlow("#lastName", "Santos");
        typeSlow("#userEmail", "jhony.jpn@gmail.com");

        clickAndPause(page.locator("label[for='gender-radio-1']"));

        typeSlow("#userNumber", "9999999999");

        typeSlow("#subjectsInput", "Maths");
        page.keyboard().press("Enter");
        shortPause();

        clickAndPause(page.locator("label[for='hobbies-checkbox-1']"));

        typeSlow("#currentAddress", "Rua das Flores, 123 - Curitiba/PR");

        typeSlow("#react-select-3-input", "NCR");
        page.keyboard().press("Enter");
        shortPause();

        typeSlow("#react-select-4-input", "Delhi");
        page.keyboard().press("Enter");
        shortPause();

        clickAndPause(page.locator("#submit"));

        page.locator("#example-modal-sizes-title-lg")
                .waitFor(new Locator.WaitForOptions().setTimeout(30_000));

        pause();

        page.keyboard().press("Escape");
        shortPause();
    }

    private void runAlertsScenario() {
        page.onceDialog(dialog -> {
            sleepSilently(1200);
            dialog.accept();
        });

        clickAndPause(page.locator("#alertButton"));

        shortPause();

        page.onceDialog(dialog -> {
            sleepSilently(1200);
            dialog.accept();
        });

        clickAndPause(page.locator("#confirmButton"));

        shortPause();
    }

    private void typeSlow(String selector, String text) {
        Locator locator = page.locator(selector).first();

        locator.scrollIntoViewIfNeeded();
        locator.click();

        try {
            locator.fill("");
        } catch (Exception ignored) {
        }

        page.keyboard().type(text, new Keyboard.TypeOptions().setDelay(55));
        shortPause();
    }

    private void clickAndPause(Locator locator) {
        locator.scrollIntoViewIfNeeded();

        try {
            locator.click(new Locator.ClickOptions().setTimeout(15_000));
        } catch (Exception e) {
            locator.click(new Locator.ClickOptions().setForce(true).setTimeout(15_000));
        }

        shortPause();
    }

    private void pause() {
        page.waitForTimeout(1800);
    }

    private void shortPause() {
        page.waitForTimeout(700);
    }

    private void sleepSilently(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}