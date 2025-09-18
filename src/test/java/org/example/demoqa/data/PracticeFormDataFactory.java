package org.example.demoqa.data;

import org.example.demoqa.models.PracticeFormData;
import org.example.demoqa.models.PracticeFormData.Gender;
import org.example.demoqa.models.PracticeFormData.Hobby;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public final class PracticeFormDataFactory {
    private PracticeFormDataFactory() {}

    public static PracticeFormData validStudent(Path pictureOrNull) {
        return new PracticeFormData(
                "Jhonatan",
                "Santos",
                "jhony.jpn@gmail.com",
                "9999999999",
                LocalDate.of(2025, 9, 18),
                List.of("Maths"),
                Set.of(Hobby.SPORTS),
                Gender.MALE,
                "Rua das Flores, 123 - Curitiba/PR",
                "NCR",
                "Delhi",
                pictureOrNull
        );
    }

    public static PracticeFormData multipleSubjectsAndHobbies() {
        return new PracticeFormData(
                "Rafael",
                "Santos",
                "rafael.santos@example.com",
                "8888888888",
                LocalDate.of(2000, 1, 1),
                List.of("English", "History"),
                Set.of(Hobby.READING, Hobby.MUSIC),
                Gender.OTHER,
                "Av. Brasil, 456 - São Paulo/SP",
                "NCR",
                "Delhi",
                null // sem upload
        );
    }
}
