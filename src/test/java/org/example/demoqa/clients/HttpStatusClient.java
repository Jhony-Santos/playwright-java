package org.example.demoqa.clients;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class HttpStatusClient implements AutoCloseable {

    private final APIRequestContext api;

    public HttpStatusClient(Playwright playwright) {
        APIRequest request = playwright.request();
        this.api = request.newContext();
    }

    public int getStatus(String url) {
        APIResponse resp = null;
        try {
            resp = api.get(url, RequestOptions.create().setTimeout(15_000));
            return resp.status();
        } finally {
            if (resp != null) resp.dispose();
        }
    }

    public boolean isOk(String url) {
        APIResponse resp = null;
        try {
            resp = api.get(url, RequestOptions.create().setTimeout(15_000));
            return resp.ok();
        } finally {
            if (resp != null) resp.dispose();
        }
    }

    @Override
    public void close() {
        if (api != null) api.dispose();
    }
}