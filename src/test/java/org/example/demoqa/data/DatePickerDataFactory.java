package org.example.demoqa.data;

import org.example.demoqa.data.models.DatePickerData;

public final class DatePickerDataFactory {
    private DatePickerDataFactory() {}

    public static DatePickerData valid() {
        return new DatePickerData(
                "March",
                "2026",
                29,
                "03/29/2026",

                "March",
                "2026",
                29,
                "13:30",
                "March 29, 2026 1:30 PM"
        );
    }

    public static DatePickerData anotherValid() {
        return new DatePickerData(
                "December",
                "2028",
                25,
                "12/25/2028",

                "December",
                "2028",
                25,
                "14:00",
                "December 25, 2028 2:00 PM"
        );
    }
}