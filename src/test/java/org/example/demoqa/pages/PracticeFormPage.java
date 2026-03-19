package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PracticeFormPage extends BasePage {

    public enum Gender { MALE, FEMALE, OTHER }
    public enum Hobby  { SPORTS, READING, MUSIC }

    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator emailInput;
    private final Locator mobileInput;
    private final Locator dobInput;
    private final Locator subjectsInput;
    private final Locator addressTextArea;
    private final Locator uploadPictureInput;
    private final Locator submitButton;

    private final Locator stateInput;
    private final Locator cityInput;

    private final Locator resultModalTitle;
    private final Locator resultModal;
    private final Locator closeLargeModal;
    private final Locator modalHeaderCloseButton;

    public PracticeFormPage(Page page) {
        super(page);
        this.firstNameInput = page.locator("#firstName");
        this.lastNameInput  = page.locator("#lastName");
        this.emailInput     = page.locator("#userEmail");
        this.mobileInput    = page.locator("#userNumber");
        this.dobInput       = page.locator("#dateOfBirthInput");
        this.subjectsInput  = page.locator("#subjectsInput");
        this.addressTextArea     = page.locator("#currentAddress");
        this.uploadPictureInput  = page.locator("#uploadPicture");
        this.submitButton        = page.locator("#submit");

        this.stateInput = page.locator("#react-select-3-input");
        this.cityInput  = page.locator("#react-select-4-input");

        this.resultModalTitle = page.locator("#example-modal-sizes-title-lg");
        this.resultModal      = page.locator(".modal-content");
        this.closeLargeModal  = page.locator("#closeLargeModal");

        // ✅ mais confiável que o botão grande de baixo
        this.modalHeaderCloseButton = page.locator(".modal-header button.close, .modal-header .close").first();
    }

    private Locator genderLabel(String value) {
        return page.locator("label[for='gender-radio-" + value + "']");
    }

    private Locator hobbyLabel(String value) {
        return page.locator("label[for='hobbies-checkbox-" + value + "']");
    }

    // --------- Actions ---------

    public PracticeFormPage fillFirstName(String first) {
        firstNameInput.fill(first);
        return this;
    }

    public PracticeFormPage fillLastName(String last) {
        lastNameInput.fill(last);
        return this;
    }

    public PracticeFormPage fillEmail(String email) {
        emailInput.fill(email);
        emailInput.dispatchEvent("blur");
        return this;
    }

    public PracticeFormPage fillMobile(String mobile) {
        mobileInput.fill(mobile);
        return this;
    }

    public PracticeFormPage setDateOfBirth(String valueAsShown) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(valueAsShown, fmt);
        return setDateOfBirth(date);
    }

    public PracticeFormPage setDateOfBirth(LocalDate date) {
        dobInput.click();

        Locator monthSelect = page.locator(".react-datepicker__month-select");
        Locator yearSelect  = page.locator(".react-datepicker__year-select");

        monthSelect.selectOption(String.valueOf(date.getMonthValue() - 1));
        yearSelect.selectOption(String.valueOf(date.getYear()));

        String day = String.format("%02d", date.getDayOfMonth());
        page.locator(".react-datepicker__day--0" + day + ":not(.react-datepicker__day--outside-month)")
                .first()
                .click();

        return this;
    }

    public PracticeFormPage addSubject(String subject) {
        subjectsInput.fill(subject);
        subjectsInput.press("Enter");
        return this;
    }

    public PracticeFormPage selectGender(String genderText) {
        switch (genderText.trim().toLowerCase()) {
            case "male" -> clickLabelSafely(genderLabel("1"));
            case "female" -> clickLabelSafely(genderLabel("2"));
            default -> clickLabelSafely(genderLabel("3"));
        }
        return this;
    }

    public PracticeFormPage selectHobby(String hobbyText) {
        switch (hobbyText.trim().toLowerCase()) {
            case "sports"  -> clickLabelSafely(hobbyLabel("1"));
            case "reading" -> clickLabelSafely(hobbyLabel("2"));
            default        -> clickLabelSafely(hobbyLabel("3"));
        }
        return this;
    }

    public PracticeFormPage selectGender(Gender gender) {
        return switch (gender) {
            case MALE -> selectGender("Male");
            case FEMALE -> selectGender("Female");
            case OTHER -> selectGender("Other");
        };
    }

    public PracticeFormPage selectHobby(Hobby hobby) {
        return switch (hobby) {
            case SPORTS -> selectHobby("Sports");
            case READING -> selectHobby("Reading");
            case MUSIC -> selectHobby("Music");
        };
    }

    public PracticeFormPage uploadPicture(Path file) {
        uploadPictureInput.setInputFiles(file);
        return this;
    }

    public PracticeFormPage uploadPicture(String path) {
        return uploadPicture(Paths.get(path));
    }

    public PracticeFormPage fillCurrentAddress(String address) {
        addressTextArea.fill(address);
        return this;
    }

    public PracticeFormPage selectState(String state) {
        stateInput.fill(state);
        stateInput.press("Enter");
        return this;
    }

    public PracticeFormPage selectCity(String city) {
        cityInput.fill(city);
        cityInput.press("Enter");
        return this;
    }

    // --------- Submit / Modal ---------

    private void waitModalOpen() {
        resultModalTitle.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(30_000));
    }

    private void clickSubmitSafely() {
        safeRemoveObstructions();
        submitButton.scrollIntoViewIfNeeded();

        try {
            submitButton.click(new Locator.ClickOptions().setTimeout(15_000));
        } catch (Exception e) {
            safeRemoveObstructions();
            submitButton.click(new Locator.ClickOptions()
                    .setForce(true)
                    .setTimeout(15_000));
        }
    }

    private void clickLabelSafely(Locator label) {
        safeRemoveObstructions();
        label.scrollIntoViewIfNeeded();

        try {
            label.click(new Locator.ClickOptions().setTimeout(10_000));
        } catch (Exception e) {
            safeRemoveObstructions();
            label.click(new Locator.ClickOptions()
                    .setForce(true)
                    .setTimeout(10_000));
        }
    }

    public PracticeFormPage submit() {
        clickSubmitSafely();
        waitModalOpen();
        return this;
    }

    public PracticeFormPage trySubmit() {
        clickSubmitSafely();
        return this;
    }

    public Locator resultModalTitle() {
        return resultModalTitle;
    }

    public Locator resultModal() {
        return resultModal;
    }

    public PracticeFormPage closeResultModal() {
        waitModalOpen();
        safeRemoveObstructions();

        // 1) tenta fechar pelo "X" do header
        try {
            if (modalHeaderCloseButton.count() > 0) {
                modalHeaderCloseButton.scrollIntoViewIfNeeded();
                modalHeaderCloseButton.click(new Locator.ClickOptions().setTimeout(5_000));
            } else {
                throw new RuntimeException("Header close button não encontrado");
            }
        } catch (Exception ignored) {
            // 2) fallback: ESC
            try {
                page.keyboard().press("Escape");
            } catch (Exception ignored2) {
                // 3) fallback final: esconde o modal via JS
                try {
                    page.evaluate("""
                        () => {
                          const modal = document.querySelector('.modal');
                          if (modal) modal.remove();
                          document.querySelectorAll('.modal-backdrop').forEach(e => e.remove());
                          document.body.classList.remove('modal-open');
                          document.body.style.removeProperty('padding-right');
                        }
                    """);
                } catch (Exception ignored3) {}
            }
        }

        resultModalTitle.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.DETACHED)
                .setTimeout(10_000));

        return this;
    }

    public boolean formIsSubmitted() {
        try {
            return resultModalTitle.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    // --------- Validations ---------

    private boolean isInvalidCss(Locator input) {
        return (Boolean) input.evaluate("el => el.matches(':invalid')");
    }

    public boolean isFirstNameInvalid() { return isInvalidCss(firstNameInput); }
    public boolean isLastNameInvalid()  { return isInvalidCss(lastNameInput); }
    public boolean isMobileInvalid()    { return isInvalidCss(mobileInput); }
    public boolean isEmailInvalid()     { return isInvalidCss(emailInput); }

    public boolean isAnyGenderSelected() {
        return (Boolean) page.evaluate("() => document.querySelectorAll(\"input[name='gender']:checked\").length > 0");
    }

    public String firstNameValidationMessage() {
        return (String) firstNameInput.evaluate("el => el.validationMessage");
    }

    public String lastNameValidationMessage() {
        return (String) lastNameInput.evaluate("el => el.validationMessage");
    }

    public String mobileValidationMessage() {
        return (String) mobileInput.evaluate("el => el.validationMessage");
    }

    public String emailValidationMessage() {
        return (String) emailInput.evaluate("el => el.validationMessage");
    }

    private boolean validityWithReport(Locator el, String prop) {
        return (Boolean) el.evaluate("(el, prop) => { el.reportValidity(); return el.validity[prop]; }", prop);
    }

    public boolean mobileValidity(String prop) {
        return validityWithReport(mobileInput, prop);
    }

    public boolean emailValidity(String prop) {
        if ("typeMismatch".equals(prop)) {
            return (Boolean) emailInput.evaluate("""
                el => {
                  el.reportValidity();
                  return el.validity.typeMismatch
                    || (el.matches(':invalid') && (el.validationMessage || '').trim().length > 0);
                }
            """);
        }
        return validityWithReport(emailInput, prop);
    }

    // --------- Values ---------

    public String mobileValue() {
        return mobileInput.inputValue();
    }
}