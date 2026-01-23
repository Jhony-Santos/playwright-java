package org.example.demoqa.data.models;

public record WebTableRowData(
        String firstName,
        String lastName,
        String email,
        int age,
        int salary,
        String department
) { }
