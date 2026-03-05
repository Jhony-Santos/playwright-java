package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

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
    }

    private Locator genderLabel(String value)  {
        return page.locator("label[for='gender-radio-" + value + "']");
    }
    private Locator hobbyLabel(String value)   {
        return page.locator("label[for='hobbies-checkbox-" + value + "']");
    }

    // --------- Actions ---------
    public PracticeFormPage fillFirstName(String first) { firstNameInput.fill(first); return this; }
    public PracticeFormPage fillLastName(String last)   { lastNameInput.fill(last);   return this; }

    public PracticeFormPage fillEmail(String email){
        emailInput.fill(email);
        emailInput.dispatchEvent("blur");
        return this;
    }

    public PracticeFormPage fillMobile(String mobile) { mobileInput.fill(mobile); return this; }

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
                .first().click();
        return this;
    }

    public PracticeFormPage addSubject(String subject) {
        subjectsInput.fill(subject);
        subjectsInput.press("Enter");
        return this;
    }

    public PracticeFormPage selectGender(String genderText) {
        switch (genderText.trim().toLowerCase()) {
            case "male" -> genderLabel("1").click();
            case "female" -> genderLabel("2").click();
            default -> genderLabel("3").click();
        }
        return this;
    }

    public PracticeFormPage selectHobby(String hobbyText) {
        switch (hobbyText.trim().toLowerCase()) {
            case "sports"  -> hobbyLabel("1").click();
            case "reading" -> hobbyLabel("2").click();
            default        -> hobbyLabel("3").click();
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

    private void waitModalOpen() {
        resultModalTitle.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
    }

    public PracticeFormPage submit() {
        submitButton.click();
        waitModalOpen();
        return this;
    }

    public PracticeFormPage trySubmit() {
        submitButton.click();
        return this;
    }

    // --------- Modal ---------
    public Locator resultModalTitle() { return resultModalTitle; }
    public Locator resultModal()      { return resultModal; }

    public PracticeFormPage closeResultModal() {
        waitModalOpen();
        removeObstructions();
        closeLargeModal.scrollIntoViewIfNeeded();
        closeLargeModal.click();
        resultModalTitle.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.DETACHED));
        return this;
    }

    public boolean formIsSubmitted() {
        try { return resultModalTitle.isVisible(); } catch (Exception e) { return false; }
    }

    // --------- ✅ Validations mais estáveis ---------

    /** Checa se um input está inválido via CSS pseudo-class :invalid (bem confiável no DemoQA). */
    private boolean isInvalidCss(Locator input) {
        return (Boolean) input.evaluate("el => el.matches(':invalid')");
    }

    public boolean isFirstNameInvalid() { return isInvalidCss(firstNameInput); }
    public boolean isLastNameInvalid()  { return isInvalidCss(lastNameInput); }
    public boolean isMobileInvalid()    { return isInvalidCss(mobileInput); }
    public boolean isEmailInvalid()     { return isInvalidCss(emailInput); }

    /** Gender no DemoQA é “custom”; validação HTML5 não é consistente. Usamos “selecionado ou não”. */
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

    /** Força reportValidity e retorna um validity[prop]. */
    private boolean validityWithReport(Locator el, String prop) {
        return (Boolean) el.evaluate("(el, prop) => { el.reportValidity(); return el.validity[prop]; }", prop);
    }

    // --------- ✅ Compat com testes/BDD antigos ---------

    /** Mantém compatibilidade com Steps antigos: mobileValidity("tooShort"), etc. */
    public boolean mobileValidity(String prop) {
        return validityWithReport(mobileInput, prop);
    }

    /** Mantém compatibilidade com checks antigos de e-mail: emailValidity("typeMismatch"), etc. */
    public boolean emailValidity(String prop) {
        return validityWithReport(emailInput, prop);
    }

    // --------- Values ---------

    public String mobileValue() {
        return mobileInput.inputValue();
    }
}