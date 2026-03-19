package org.example.demoqa.support;

import com.microsoft.playwright.Page;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TestAsserts {
    private TestAsserts() {}

    /** Assert de "path" exato (tolerante a barra final). */
    public static void assertOnPath(Page page, String expectedPath) {
        String expected = normalizeExpectedPath(expectedPath);

        URI uri = URI.create(page.url());
        String actual = uri.getPath();               // ex: "/elements"
        actual = normalizeActualPath(actual);

        assertEquals(expected, actual,
                "Path esperado: " + expected + " | Atual: " + actual + " | URL: " + page.url());
    }

    private static String normalizeExpectedPath(String path) {
        if (path == null || path.isBlank()) throw new IllegalArgumentException("path não pode ser vazio");
        String p = path.trim();
        if (!p.startsWith("/")) p = "/" + p;
        if (p.endsWith("/") && p.length() > 1) p = p.substring(0, p.length() - 1);
        return p;
    }

    private static String normalizeActualPath(String path) {
        if (path == null || path.isBlank()) return "/";
        String p = path;
        if (p.endsWith("/") && p.length() > 1) p = p.substring(0, p.length() - 1);
        return p;
    }
}