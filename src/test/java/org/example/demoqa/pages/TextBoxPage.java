package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.demoqa.models.TextBoxData;

import static com.microsoft.playwright.options.AriaRole.BUTTON;

public class TextBoxPage extends BasePage {

    // --- Locators IMUTÁVEIS (final) ---
    private final Locator userNameInput;
    private final Locator userEmailInput;
    private final Locator currentAddressInput;
    private final Locator permanentAddressInput;
    private final Locator submitButton;
    private final Locator outputBox;

    public TextBoxPage(Page page) {
        super(page);
        // IDs do HTML do DemoQA
        this.userNameInput = page.locator("#userName");
        this.userEmailInput = page.locator("#userEmail");
        this.currentAddressInput = page.locator("#currentAddress");
        this.permanentAddressInput= page.locator("#permanentAddress");
        this.submitButton = page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.outputBox = page.locator("#output");
    }

    /** DRY: um único método para preencher o formulário */
    public TextBoxPage fillForm(String name, String email, String currentAddress, String permanentAddress) {
        userNameInput.fill(name);
        userEmailInput.fill(email);
        currentAddressInput.fill(currentAddress);
        if (permanentAddress != null) {
            permanentAddressInput.fill(permanentAddress);
        }
        return this;
    }

    // Overload para DTO (novo)
    public TextBoxPage fillForm(TextBoxData data) {
        return fillForm(data.name(), data.email(), data.currentAddress(), data.permanentAddress());
    }




    public TextBoxPage submit() {
        submitButton.click();
        return this;
    }

    // Exponha o estado para o teste fazer asserções sincronizadas
    public Locator outputBox() {
        return outputBox;
    }
}
