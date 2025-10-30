package org.example.demoqa.data.models;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record PracticeFormData(
        String firstName,
        String lastName,
        String email,
        String mobile,
        LocalDate dateOfBirth,
        List<String> subjects,
        Set<Hobby> hobbies,
        Gender gender,
        String address,
        String state,
        String city,
        Path picture
) {
    public enum Gender { MALE, FEMALE, OTHER }
    public enum Hobby  { SPORTS, READING, MUSIC }
}
