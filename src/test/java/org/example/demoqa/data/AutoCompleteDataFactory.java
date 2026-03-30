package org.example.demoqa.data;

import org.example.demoqa.data.models.AutoCompleteData;

import java.util.List;

public final class AutoCompleteDataFactory {
    private AutoCompleteDataFactory() {}

    public static AutoCompleteData valid() {
        return new AutoCompleteData(
                List.of("Blue", "Green"),
                "Red"
        );
    }

    public static AutoCompleteData anotherValid() {
        return new AutoCompleteData(
                List.of("Black", "White"),
                "Yellow"
        );
    }

    public static AutoCompleteData singleOnly() {
        return new AutoCompleteData(
                List.of(),
                "Red"
        );
    }
}