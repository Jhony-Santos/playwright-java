package org.example.demoqa.data.models;

public record DatePickerData(
        String selectDateMonth,
        String selectDateYear,
        int selectDateDay,
        String selectDateExpectedValue,

        String dateTimeMonth,
        String dateTimeYear,
        int dateTimeDay,
        String dateTimeTime,
        String dateTimeExpectedValue
) {}