package org.example.demoqa.clients;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

import java.net.URI;

public class HttpStatusClient implements AutoCloseable {

    private static final String DEFAULT_BASE_URL = "https://demoqa.com";

    private final APIRequestContext api;

    public HttpStatusClient(Playwright playwright) {
        APIRequest request = playwright.request();
        this.api = request.newContext();
    }

    public int getStatus(String url) {
        String resolvedUrl = toAbsoluteUrl(url);

        APIResponse resp = null;
        try {
            resp = api.get(resolvedUrl, RequestOptions.create().setTimeout(15_000));
            return resp.status();
        } finally {
            if (resp != null) resp.dispose();
        }
    }

    public boolean isOk(String url) {
        String resolvedUrl = toAbsoluteUrl(url);

        APIResponse resp = null;
        try {
            resp = api.get(resolvedUrl, RequestOptions.create().setTimeout(15_000));
            return resp.ok();
        } finally {
            if (resp != null) resp.dispose();
        }
    }

    private String toAbsoluteUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL não pode ser nula ou vazia.");
        }

        String trimmed = url.trim();

        // já é absoluta
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }

        // relativa -> resolve contra demoqa
        return URI.create(DEFAULT_BASE_URL).resolve(trimmed).toString();
    }

    @Override
    public void close() {
        if (api != null) api.dispose();
    }
}