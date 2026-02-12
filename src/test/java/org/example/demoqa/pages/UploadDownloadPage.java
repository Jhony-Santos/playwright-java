package org.example.demoqa.pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.nio.file.Path;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UploadDownloadPage extends BasePage {

    private final Locator downloadButton;
    private final Locator uploadInput;
    private final Locator uploadedFilePath;

    public UploadDownloadPage(Page page) {
        super(page);
        this.downloadButton = page.locator("#downloadButton");
        this.uploadInput = page.locator("#uploadFile");
        this.uploadedFilePath = page.locator("#uploadedFilePath");
    }

    /** Upload via input[type=file] + validação do texto exibido na página */
    public UploadDownloadPage uploadFile(Path file) {
        removeObstructions();
        assertThat(uploadInput).isVisible();

        uploadInput.setInputFiles(file);

        // valida que o DemoQA mostrou o arquivo
        assertThat(uploadedFilePath).isVisible();
        assertThat(uploadedFilePath).containsText(file.getFileName().toString());

        return this;
    }

    /** Captura o Download do Playwright (sincronizado) */
    public Download download() {
        removeObstructions();
        assertThat(downloadButton).isVisible();
        return page.waitForDownload(() -> downloadButton.click());
    }

    /** Expor o locator para asserts adicionais se você quiser */
    public Locator uploadedFilePath() {
        return uploadedFilePath;
    }
}
