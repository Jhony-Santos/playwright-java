package org.example.demoqa.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FramesPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/frames/?(\\?.*)?$");
    private static final String EXPECTED_HEADING = "This is a sample page";

    public FramesPage(Page page) {
        super(page);
    }

    private Locator frame1() { return page.locator("#frame1"); }
    private Locator frame2() { return page.locator("#frame2"); }

    private FrameLocator frame1Content() { return page.frameLocator("#frame1"); }
    private FrameLocator frame2Content() { return page.frameLocator("#frame2"); }

    private Locator frame1Heading() { return frame1Content().locator("#sampleHeading"); }
    private Locator frame2Heading() { return frame2Content().locator("#sampleHeading"); }

    public FramesPage assertPageLoaded() {
        safeRemoveObstructions();

        ensureAppIsUp(List.of("body", "#frame1", "#frame2"), 60_000, true);

        assertThat(page).hasURL(URL_REGEX);
        assertThat(frame1()).isVisible();
        assertThat(frame2()).isVisible();

        try {
            Locator header = page.locator("div.main-header, h1").first();
            header.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5_000));
            assertThat(header).containsText("Frames");
        } catch (Exception ignored) {
        }

        return this;
    }

    public FramesPage assertFrame1Content() {
        Locator heading = frame1Heading();

        heading.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        assertThat(heading).containsText(EXPECTED_HEADING);
        return this;
    }

    public FramesPage assertFrame2Content() {
        Locator heading = frame2Heading();

        heading.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        assertThat(heading).containsText(EXPECTED_HEADING);
        return this;
    }

    public FramesPage assertFramesHaveUsefulSize() {
        Locator frame1 = frame1();
        Locator frame2 = frame2();

        frame1.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        frame2.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        var box1 = frame1.boundingBox();
        var box2 = frame2.boundingBox();

        if (box1 == null || box1.width <= 0 || box1.height <= 0) {
            throw new AssertionError("Frame1 sem dimensões renderizadas úteis. box=" + box1);
        }

        if (box2 == null || box2.width <= 0 || box2.height <= 0) {
            throw new AssertionError("Frame2 sem dimensões renderizadas úteis. box=" + box2);
        }

        return this;
    }
}