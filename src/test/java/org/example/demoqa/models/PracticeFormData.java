package org.example.demoqa.models;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record PracticeFormData(
        String firstName,
        String lastName,
        String email,
        String mobile,
        LocalDate dateOfBirth,        // ex.: LocalDate.of(2000, 1, 1)
        List<String> subjects,        // ex.: List.of("Maths", "English")
        Set<Hobby> hobbies,           // ex.: Set.of(Hobby.SPORTS, Hobby.MUSIC)
        Gender gender,                // MALE, FEMALE, OTHER
        String address,
        String state,                 // ex.: "NCR"
        String city,                  // ex.: "Delhi"
        Path picture                  // pode ser null se não quiser upload
) {
    public enum Gender { MALE, FEMALE, OTHER }
    public enum Hobby  { SPORTS, READING, MUSIC }
}
