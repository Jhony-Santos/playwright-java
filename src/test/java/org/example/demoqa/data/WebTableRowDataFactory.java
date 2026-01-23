package org.example.demoqa.data;

import org.example.demoqa.data.models.WebTableRowData;

public class WebTableRowDataFactory {

    public static WebTableRowData valid() {
        return new WebTableRowData(
                "Carlos",
                "Silva",
                "carlos.silva@example.com",
                32,
                8500,
                "Engineering"
        );
    }

    public static WebTableRowData anotherValid() {
        return new WebTableRowData(
                "Julia",
                "Souza",
                "julia.souza@example.com",
                27,
                9200,
                "QA"
        );
    }
}
