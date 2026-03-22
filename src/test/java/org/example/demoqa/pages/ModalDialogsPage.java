package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ModalDialogsPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/modal-dialogs/?(\\?.*)?$");

    public ModalDialogsPage(Page page) {
        super(page);
    }

    private Locator smallModalButton() { return page.locator("#showSmallModal"); }
    private Locator largeModalButton() { return page.locator("#showLargeModal"); }

    private Locator modalContent() { return page.locator(".modal-content").first(); }
    private Locator modalTitle() { return page.locator(".modal-title").first(); }
    private Locator modalBody() { return page.locator(".modal-body").first(); }
    private Locator closeSmallModalButton() { return page.locator("#closeSmallModal").first(); }
    private Locator closeLargeModalButton() { return page.locator("#closeLargeModal").first(); }

    public ModalDialogsPage assertPageLoaded() {
        safeRemoveObstructions();

        ensureAppIsUp(List.of("body", "#showSmallModal", "#showLargeModal"), 60_000, true);

        assertThat(page).hasURL(URL_REGEX);
        assertThat(smallModalButton()).isVisible();
        assertThat(largeModalButton()).isVisible();

        try {
            Locator header = page.locator("div.main-header, h1").first();
            header.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5_000));
            assertThat(header).containsText("Modal Dialogs");
        } catch (Exception ignored) {
        }

        return this;
    }

    public ModalDialogsPage openSmallModal() {
        click(smallModalButton());

        modalContent().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        return this;
    }

    public ModalDialogsPage openLargeModal() {
        click(largeModalButton());

        modalContent().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        return this;
    }

    public ModalDialogsPage assertSmallModalContent() {
        assertThat(modalTitle()).containsText("Small Modal");
        assertThat(modalBody()).containsText("This is a small modal");
        return this;
    }

    public ModalDialogsPage assertLargeModalContent() {
        assertThat(modalTitle()).containsText("Large Modal");
        assertThat(modalBody()).containsText("Lorem Ipsum");
        return this;
    }

    public ModalDialogsPage closeSmallModal() {
        click(closeSmallModalButton());

        modalContent().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(10_000));

        return this;
    }

    public ModalDialogsPage closeLargeModal() {
        click(closeLargeModalButton());

        modalContent().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(10_000));

        return this;
    }
}