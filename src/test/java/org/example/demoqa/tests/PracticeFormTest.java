package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.data.PracticeFormDataFactory;
import org.example.demoqa.flows.PracticeFormSteps;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.PracticeFormPage;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PracticeFormTest extends BaseTest {

    /** Resolve um arquivo do classpath (src/test/resources). */
    private Path resourcePath(String classpathLocation) {
        try {
            URL url = Objects.requireNonNull(
                    getClass().getClassLoader().getResource(classpathLocation),
                    "Resource not found on classpath: " + classpathLocation
            );
            return Paths.get(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve resource: " + classpathLocation, e);
        }
    }

    @Test
    void shouldSubmitPracticeForm_withBasicData() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        var data = PracticeFormDataFactory.validStudent(
                resourcePath("files/photo.png") // ou null se não quiser upload
        );

        PracticeFormSteps.fillForm(form, data);
        form.submit();

        assertThat(form.resultModalTitle()).hasText("Thanks for submitting the form");
        assertThat(form.resultModal()).containsText(data.firstName() + " " + data.lastName());
        assertThat(form.resultModal()).containsText(data.email());
        assertThat(form.resultModal()).containsText(data.mobile());

        form.closeResultModal();
    }

    @Test
    void shouldSubmitPracticeForm_withMultipleSubjectsAndHobbies() {
        PracticeFormPage form = new HomePage(page)
                .gotoHome()
                .openForms()
                .openPracticeForm();

        var data = PracticeFormDataFactory.multipleSubjectsAndHobbies();

        PracticeFormSteps.fillForm(form, data);
        form.submit();

        assertThat(form.resultModalTitle()).hasText("Thanks for submitting the form");
        assertThat(form.resultModal()).containsText(data.firstName() + " " + data.lastName());
        assertThat(form.resultModal()).containsText(data.email());

        form.closeResultModal();
    }
}
