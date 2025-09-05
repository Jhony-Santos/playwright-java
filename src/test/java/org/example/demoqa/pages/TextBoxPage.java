package org.example.demoqa.pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextBoxPage extends BasePage {

    private  String userName = "userName";
    private  String userEmail = "userEmail-label";
    private  String userAddress = "currentAddress";
    private  String userPermAddress = "userPermAddress";
    private  String submitBtn = "submitBtn";

    public TextBoxPage(Page page) { super(page); }

    public TextBoxPage fillForm(String name, String email, String curr, String perm) {
        page.getByTestId(userName).fill(name);

        //page.getByPlaceholder("Full Name").fill(name);
        page.getByPlaceholder("name@example.com").fill(email);
        page.getByTestId(userAddress).fill(curr);
        return this;
    }

    public TextBoxPage fillForm2(String name, String email, String curr, String perm) {
        page.getByTestId(userName).fill(name);
        //page.getByPlaceholder("Full Name").fill(name);
        page.getByPlaceholder("name@example.com").fill(email);
        page.locator("#currentAddress").fill(curr);
        page.locator("#permanentAddress").fill(perm);
        return this;
    }

    public TextBoxPage submit() {
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        return this;
    }

    public void assertOutputContains(String... texts) {
        String out = page.locator("#output").innerText();
        for (String t : texts) assertTrue(out.contains(t));
    }
}
