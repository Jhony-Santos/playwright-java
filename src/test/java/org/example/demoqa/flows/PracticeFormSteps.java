package org.example.demoqa.flows;

import org.example.demoqa.models.PracticeFormData;
import org.example.demoqa.pages.PracticeFormPage;

import java.time.format.DateTimeFormatter;

public final class PracticeFormSteps {
    private PracticeFormSteps() {}

    private static final DateTimeFormatter DOB_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }

    /** Preenche o formulário com base no DTO e retorna a própria page para chaining. */
    public static PracticeFormPage fillForm(PracticeFormPage form, PracticeFormData data) {
        // Nome / Email / Mobile (preenche só se houver)
        if (hasText(data.firstName())) form.fillFirstName(data.firstName().trim());
        if (hasText(data.lastName()))  form.fillLastName(data.lastName().trim());
        if (hasText(data.email()))     form.fillEmail(data.email().trim());
        if (hasText(data.mobile()))    form.fillMobile(data.mobile().trim());

        // Gênero
        if (data.gender() != null) {
            form.selectGender(switch (data.gender()) {
                case MALE   -> "Male";
                case FEMALE -> "Female";
                case OTHER  -> "Other";
            });
        }

        // Data de nascimento
        if (data.dateOfBirth() != null) {
            form.setDateOfBirth(data.dateOfBirth().format(DOB_FMT));
        }

        // Subjects
        if (data.subjects() != null) {
            data.subjects().stream()
                    .filter(PracticeFormSteps::hasText)
                    .map(String::trim)
                    .forEach(form::addSubject);
        }

        // Hobbies
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

        // Endereço / Estado / Cidade
        if (hasText(data.address())) form.fillCurrentAddress(data.address().trim());
        if (hasText(data.state()))   form.selectState(data.state().trim());
        if (hasText(data.city()))    form.selectCity(data.city().trim());

        return form;
    }

    /** Conveniência: preenche e envia. */
    public static PracticeFormPage fillFormAndSubmit(PracticeFormPage form, PracticeFormData data) {
        return fillForm(form, data).submit();
    }
}
