package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PracticeFormPage extends BasePage {

    // Enums opcionais para quem preferir tipado
    public enum Gender { MALE, FEMALE, OTHER }
    public enum Hobby  { SPORTS, READING, MUSIC }

    // ---------- Locators ----------
    private Locator firstNameInput;
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

    // ---------- Ctor ----------
    public PracticeFormPage(Page page) {
        super(page);
        this.firstNameInput = page.locator("#firstName");
        this.lastNameInput  = page.locator("#lastName");
        this.emailInput = page.locator("#userEmail");
        this.mobileInput = page.locator("#userNumber");
        this.dobInput = page.locator("#dateOfBirthInput");
        this.subjectsInput = page.locator("#subjectsInput");
        this.addressTextArea = page.locator("#currentAddress");
        this.uploadPictureInput = page.locator("#uploadPicture");
        this.submitButton  = page.locator("#submit");

        this.stateInput         = page.locator("#react-select-3-input");
        this.cityInput          = page.locator("#react-select-4-input");

        this.resultModalTitle   = page.locator("#example-modal-sizes-title-lg");
        this.resultModal        = page.locator(".modal-content");
        this.closeLargeModal    = page.locator("#closeLargeModal");
    }

    // ---------- Helpers ----------
    private Locator genderLabel(String value)  {
        return page.locator("label[for='gender-radio-"    + value + "']");
    }


    private Locator hobbyLabel(String value)   {
        return page.locator("label[for='hobbies-checkbox-" + value + "']");
    }

    // ---------- Actions (fluent) ----------
    public PracticeFormPage fillFirstName(String first)   { firstNameInput.fill(first); return this; }
    public PracticeFormPage fillLastName(String last)     { lastNameInput.fill(last);   return this; }
    public PracticeFormPage fillEmail(String email)       { emailInput.fill(email);     return this; }
    public PracticeFormPage fillMobile(String mobile)     { mobileInput.fill(mobile);   return this; }

    /** Define a data exatamente como o input exibe (ex.: "18 Sep 2025"). */
    public PracticeFormPage setDateOfBirth(String valueAsShown) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(valueAsShown, fmt);
        return setDateOfBirth(date);
    }

    /** Versão tipada. */
    public PracticeFormPage setDateOfBirth(LocalDate date) {
        // abre o datepicker
        dobInput.click();

        // seleciona mês e ano (os selects nativos do datepicker do DemoQA)
        Locator monthSelect = page.locator(".react-datepicker__month-select");
        Locator yearSelect  = page.locator(".react-datepicker__year-select");

        // month-select aceita valores 0..11
        monthSelect.selectOption(String.valueOf(date.getMonthValue() - 1));
        yearSelect.selectOption(String.valueOf(date.getYear()));

        // escolhe o dia, evitando dias da outra folha do calendário
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

    // ---------- Gender/Hobby com String (recomendado p/ desacoplar a Page) ----------
    public PracticeFormPage selectGender(String genderText) {
        switch (genderText.trim().toLowerCase()) {
            case "male"   -> genderLabel("1").click();
            case "female" -> genderLabel("2").click();
            default       -> genderLabel("3").click();
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

    // ---------- Overloads com Enum (se quiser usar tipado no teste) ----------
    public PracticeFormPage selectGender(Gender gender) {
        return switch (gender) {
            case MALE   -> selectGender("Male");
            case FEMALE -> selectGender("Female");
            case OTHER  -> selectGender("Other");
        };
    }

    public PracticeFormPage selectHobby(Hobby hobby) {
        return switch (hobby) {
            case SPORTS  -> selectHobby("Sports");
            case READING -> selectHobby("Reading");
            case MUSIC   -> selectHobby("Music");
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

    public PracticeFormPage submit() {
        submitButton.click();
        // espera pelo modal para reduzir flakiness
        assertThat(resultModalTitle).isVisible();
        return this;
    }

    // ---------- Exposições para asserts ----------
    public Locator resultModalTitle() { return resultModalTitle; }
    public Locator resultModal()      { return resultModal; }
    public PracticeFormPage closeResultModal() { closeLargeModal.click(); return this; }
}
