package org.example.demoqa.data;

import org.example.demoqa.data.models.SelectMenuData;

import java.util.List;

public final class SelectMenuDataFactory {
    private SelectMenuDataFactory() {}

    public static SelectMenuData valid() {
        return new SelectMenuData(
                "Another root option",
                "Dr.",
                "Purple",
                List.of("Green", "Blue"),
                List.of("volvo", "audi")
        );
    }
}