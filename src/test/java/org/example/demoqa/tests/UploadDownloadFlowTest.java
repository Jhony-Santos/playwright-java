package org.example.demoqa.tests;

import com.microsoft.playwright.Download;
import org.example.demoqa.BaseTest;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.UploadDownloadPage;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

    public class UploadDownloadFlowTest extends BaseTest {

    @Test
    void shouldUploadFileAndDownloadSample() throws Exception {

        // 1) Navegação até a tela
        UploadDownloadPage uploadDownload = new HomePage(page)
                .gotoHome()
                .openElements()
                .openUploadDownload();

        // 2) Upload
        Path fileToUpload = Paths.get("src", "test", "resources", "files", "upload.txt");
        assertTrue(Files.exists(fileToUpload), "Arquivo de upload não existe: " + fileToUpload);

        uploadDownload.uploadFile(fileToUpload);

        // 3) Download
        Download download = uploadDownload.download();

        // 4) Salvar manualmente
        Path saved = Paths.get("target", "downloads", download.suggestedFilename());

        Files.createDirectories(saved.getParent()); // garante pasta
        download.saveAs(saved);

        // 5) Validação
        assertTrue(Files.exists(saved), "Arquivo baixado não existe: " + saved);
        assertTrue(Files.size(saved) > 0, "Arquivo baixado está vazio: " + saved);
    }
}