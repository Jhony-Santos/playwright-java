package org.example.demoqa.data.models;

import java.util.List;

public record SelectMenuData(
        String selectValue,
        String selectOne,
        String oldStyleColor,
        List<String> multiSelectDropDown,
        List<String> standardMultiSelect
) {}