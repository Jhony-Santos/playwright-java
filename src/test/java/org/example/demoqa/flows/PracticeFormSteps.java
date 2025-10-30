package org.example.demoqa.flows;

import org.example.demoqa.data.models.PracticeFormData;
import org.example.demoqa.pages.PracticeFormPage;

public final class PracticeFormSteps {
    private PracticeFormSteps() {}

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }

    public static PracticeFormPage fillForm(PracticeFormPage form, PracticeFormData data) {
        // Text fields
        if (hasText(data.firstName())) form.fillFirstName(data.firstName().trim());
        if (hasText(data.lastName()))  form.fillLastName(data.lastName().trim());
        if (hasText(data.email()))     form.fillEmail(data.email().trim());
        if (hasText(data.mobile()))    form.fillMobile(data.mobile().trim());

        // Gender (DTO enum -> String da UI)
        if (data.gender() != null) {
            form.selectGender(switch (data.gender()) {
                case MALE   -> "Male";
                case FEMALE -> "Female";
                case OTHER  -> "Other";
            });
        }

        // Date of Birth (LocalDate direto – sua Page já abre o datepicker)
        if (data.dateOfBirth() != null) {
            form.setDateOfBirth(data.dateOfBirth());
        }

        // Subjects
        if (data.subjects() != null) {
            data.subjects().stream()
                    .filter(PracticeFormSteps::hasText)
                    .map(String::trim)
                    .forEach(form::addSubject);
        }

        // Hobbies (DTO enum -> String da UI)
        if (data.hobbies() != null) {
            data.hobbies().forEach(h -> {
                switch (h) {
                    case SPORTS  -> form.selectHobby("Sports");
                    case READING -> form.selectHobby("Reading");
                    case MUSIC   -> form.selectHobby("Music");
                }
            });
        }

        // Upload
        if (data.picture() != null) {
            form.uploadPicture(data.picture());
        }

        // Address / State / City
        if (hasText(data.address())) form.fillCurrentAddress(data.address().trim());
        if (hasText(data.state()))   form.selectState(data.state().trim());
        if (hasText(data.city()))    form.selectCity(data.city().trim());

        return form;
    }

    public static PracticeFormPage fillFormAndSubmit(PracticeFormPage form, PracticeFormData data) {
        return fillForm(form, data).submit();
    }
}
