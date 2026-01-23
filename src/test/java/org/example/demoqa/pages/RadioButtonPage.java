package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.regex.Pattern;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class RadioButtonPage extends BasePage {

    private static final Pattern URL_PATTERN = Pattern.compile(".*/radio-button");

    public RadioButtonPage(Page page) {
        super(page);
    }

    public void assertPageLoaded() {
        assertThat(page).hasURL(URL_PATTERN);
    }


    // --- Locators ---

    private Locator yesLabel() {
        return page.locator("label[for='yesRadio']");
    }

    private Locator impressiveLabel() {
        return page.locator("label[for='impressiveRadio']");
    }

    private Locator noInput() {
        return page.locator("input#noRadio");
    }

    private Locator resultParagraph() {
        // <p class="mt-3">You have selected <span class="text-success">Yes</span></p>
        return page.locator("p.mt-3");
    }

    private Locator selectedOptionSpan() {
        return page.locator("p.mt-3 span.text-success");
    }

    // --- Ações ---

    public RadioButtonPage selectYes() {
        yesLabel().click();
        return this;
    }

    public RadioButtonPage selectImpressive() {
        impressiveLabel().click();
        return this;
    }

    public boolean isNoDisabled() {
        return noInput().isDisabled();
    }

    // --- Helpers de leitura de resultado ---

    public String resultMessage() {
        return resultParagraph().textContent().trim();
    }

    public String selectedOption() {
        return selectedOptionSpan().textContent().trim();
    }

    public boolean isResultYes() {
        return "Yes".equalsIgnoreCase(selectedOption());
    }

    public boolean isResultImpressive() {
        return "Impressive".equalsIgnoreCase(selectedOption());
    }


}
