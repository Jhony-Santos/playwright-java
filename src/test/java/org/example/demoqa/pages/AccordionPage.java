package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AccordionPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/accordian/?(\\?.*)?$");

    private static final String SECTION_1_TITLE = "What is Lorem Ipsum?";
    private static final String SECTION_2_TITLE = "Where does it come from?";
    private static final String SECTION_3_TITLE = "Why do we use it?";

    private static final String SECTION_1_TEXT = "Lorem Ipsum is simply dummy text";
    private static final String SECTION_2_TEXT = "Contrary to popular belief";
    private static final String SECTION_3_TEXT = "It is a long established fact";

    public AccordionPage(Page page) {
        super(page);
    }

    private Locator section1Heading() {
        return page.locator("button.accordion-button")
                .filter(new Locator.FilterOptions().setHasText(SECTION_1_TITLE))
                .first();
    }

    private Locator section2Heading() {
        return page.locator("button.accordion-button")
                .filter(new Locator.FilterOptions().setHasText(SECTION_2_TITLE))
                .first();
    }

    private Locator section3Heading() {
        return page.locator("button.accordion-button")
                .filter(new Locator.FilterOptions().setHasText(SECTION_3_TITLE))
                .first();
    }

    private Locator section1Content() {
        return page.locator(".accordion-body")
                .filter(new Locator.FilterOptions().setHasText(SECTION_1_TEXT))
                .first();
    }

    private Locator section2Content() {
        return page.locator(".accordion-body")
                .filter(new Locator.FilterOptions().setHasText(SECTION_2_TEXT))
                .first();
    }

    private Locator section3Content() {
        return page.locator(".accordion-body")
                .filter(new Locator.FilterOptions().setHasText(SECTION_3_TEXT))
                .first();
    }

    public AccordionPage assertPageLoaded() {
        safeRemoveObstructions();

        ensureAppIsUp(List.of("body"), 60_000, true);

        assertThat(page).hasURL(URL_REGEX);
        assertThat(section1Heading()).isVisible();
        assertThat(section2Heading()).isVisible();
        assertThat(section3Heading()).isVisible();

        return this;
    }

    public AccordionPage assertFirstSectionOpenByDefault() {
        assertThat(section1Heading()).hasAttribute("aria-expanded", "true");
        assertThat(section1Content()).containsText(SECTION_1_TEXT);
        return this;
    }

    public AccordionPage openSecondSection() {
        click(section2Heading());
        assertThat(section2Heading()).hasAttribute("aria-expanded", "true");
        assertThat(section2Content()).containsText(SECTION_2_TEXT);
        return this;
    }

    public AccordionPage openThirdSection() {
        click(section3Heading());
        assertThat(section3Heading()).hasAttribute("aria-expanded", "true");
        assertThat(section3Content()).containsText(SECTION_3_TEXT);
        return this;
    }

    public AccordionPage assertSecondSectionContent() {
        assertThat(section2Content()).containsText(SECTION_2_TEXT);
        return this;
    }

    public AccordionPage assertThirdSectionContent() {
        assertThat(section3Content()).containsText(SECTION_3_TEXT);
        return this;
    }
}