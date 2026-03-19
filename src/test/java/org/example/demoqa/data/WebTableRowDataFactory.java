package org.example.demoqa.data;

import org.example.demoqa.data.models.WebTableRowData;

public class WebTableRowDataFactory {

    public static WebTableRowData valid() {
        long suffix = System.currentTimeMillis();

        return new WebTableRowData(
                "Carlos",
                "Silva",
                "carlos.silva." + suffix + "@example.com",
                32,
                8500,
                "Engineering"
        );
    }

    public static WebTableRowData anotherValid() {
        long suffix = System.currentTimeMillis();

        return new WebTableRowData(
                "Julia",
                "Souza",
                "julia.souza." + suffix + "@example.com",
                27,
                9200,
                "QA"
        );
    }
}