package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.demoqa.data.models.TextBoxData;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.BUTTON;

public class TextBoxPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/text-box/?(\\?.*)?$");

    private final Locator userNameInput;
    private final Locator userEmailInput;
    private final Locator currentAddressInput;
    private final Locator permanentAddressInput;
    private final Locator submitButton;
    private final Locator outputBox;

    public TextBoxPage(Page page) {
        super(page);
        this.userNameInput = page.locator("#userName");
        this.userEmailInput = page.locator("#userEmail");
        this.currentAddressInput = page.locator("#currentAddress");
        this.permanentAddressInput = page.locator("#permanentAddress");
        this.submitButton = page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.outputBox = page.locator("#output");
    }

    public TextBoxPage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(userNameInput).isVisible();
        return this;
    }

    public TextBoxPage fillForm(String name, String email, String currentAddress, String permanentAddress) {
        userNameInput.fill(name);
        userEmailInput.fill(email);
        currentAddressInput.fill(currentAddress);
        if (permanentAddress != null) {
            permanentAddressInput.fill(permanentAddress);
        }
        return this;
    }

    public TextBoxPage fillForm(TextBoxData data) {
        return fillForm(data.name(), data.email(), data.currentAddress(), data.permanentAddress());
    }

    public TextBoxPage submit() {
        removeObstructions();
        submitButton.click();
        assertThat(outputBox).isVisible();
        return this;
    }

    public Locator outputBox() {
        return outputBox;
    }
}