package org.example.demoqa.clients;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;

public class HttpStatusClient implements AutoCloseable {

    private final APIRequestContext api;

    public HttpStatusClient(Playwright playwright) {
        APIRequest request = playwright.request();
        this.api = request.newContext();
    }

    public int getStatus(String url) {
        APIResponse resp = api.get(url);
        return resp.status();
    }

    public boolean isOk(String url) {
        APIResponse resp = api.get(url);
        return resp.ok();
    }

    @Override
    public void close() {
        if (api != null) api.dispose();
    }
}
