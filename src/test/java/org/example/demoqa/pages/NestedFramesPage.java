package org.example.demoqa.pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NestedFramesPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/nestedframes/?(\\?.*)?$");

    public NestedFramesPage(Page page) {
        super(page);
    }

    private Locator parentFrameElement() {
        return page.locator("#frame1");
    }

    private FrameLocator parentFrame() {
        return page.frameLocator("#frame1");
    }

    private FrameLocator childFrame() {
        return parentFrame().frameLocator("iframe");
    }

    private Locator parentFrameBody() {
        return parentFrame().locator("body");
    }

    private Locator childFrameBody() {
        return childFrame().locator("body");
    }

    public NestedFramesPage assertPageLoaded() {
        safeRemoveObstructions();

        ensureAppIsUp(List.of("body", "#frame1"), 60_000, true);

        assertThat(page).hasURL(URL_REGEX);
        assertThat(parentFrameElement()).isVisible();

        try {
            Locator header = page.locator("div.main-header, h1").first();
            header.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5_000));
            assertThat(header).containsText("Nested Frames");
        } catch (Exception ignored) {
        }

        return this;
    }

    public NestedFramesPage assertParentFrameText() {
        Locator parentBody = parentFrameBody();

        parentBody.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        assertThat(parentBody).containsText("Parent frame");
        return this;
    }

    public NestedFramesPage assertChildFrameText() {
        Locator childBody = childFrameBody();

        childBody.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10_000));

        assertThat(childBody).containsText("Child Iframe");
        return this;
    }

    public NestedFramesPage assertParentAndChildFrames() {
        return assertParentFrameText()
                .assertChildFrameText();
    }
}