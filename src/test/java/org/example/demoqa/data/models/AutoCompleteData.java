package org.example.demoqa.data.models;

import java.util.List;

public record AutoCompleteData(
        List<String> multipleColors,
        String singleColor
) {}