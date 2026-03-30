package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.demoqa.data.models.AutoCompleteData;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AutoCompletePage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/auto-complete/?(\\?.*)?$");

    private final Locator multipleInput;
    private final Locator singleInput;

    private final Locator multipleLabels;
    private final Locator multipleChips;
    private final Locator singleValue;

    public AutoCompletePage(Page page) {
        super(page);
        this.multipleInput = page.locator("#autoCompleteMultipleInput");
        this.singleInput = page.locator("#autoCompleteSingleInput");

        this.multipleLabels = page.locator(".auto-complete__multi-value__label");
        this.multipleChips = page.locator(".auto-complete__multi-value");
        this.singleValue = page.locator(".auto-complete__single-value");
    }

    public AutoCompletePage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(multipleInput).isVisible();
        assertThat(singleInput).isVisible();
        return this;
    }

    public AutoCompletePage selectMultipleColor(String color) {
        removeObstructionsSafe();
        click(multipleInput);
        multipleInput.fill(color);
        stepDelay();
        selectFromAutocomplete(color, true);
        return this;
    }

    public AutoCompletePage selectMultipleColors(List<String> colors) {
        for (String color : colors) {
            selectMultipleColor(color);
        }
        return this;
    }

    public AutoCompletePage selectSingleColor(String color) {
        removeObstructionsSafe();
        click(singleInput);
        singleInput.fill(color);
        stepDelay();
        selectFromAutocomplete(color, false);
        return this;
    }

    public AutoCompletePage selectColors(AutoCompleteData data) {
        selectMultipleColors(data.multipleColors());
        selectSingleColor(data.singleColor());
        return this;
    }

    public AutoCompletePage removeMultipleColor(String color) {
        Locator chip = multipleChips
                .filter(new Locator.FilterOptions().setHasText(color))
                .first();

        Locator removeButton = chip.locator(".auto-complete__multi-value__remove");
        click(removeButton);
        return this;
    }

    public AutoCompletePage assertMultipleColorSelected(String color) {
        assertThat(
                multipleLabels.filter(new Locator.FilterOptions().setHasText(color)).first()
        ).isVisible();
        return this;
    }

    public AutoCompletePage assertMultipleColorsSelected(List<String> colors) {
        for (String color : colors) {
            assertMultipleColorSelected(color);
        }
        return this;
    }

    public AutoCompletePage assertMultipleColorNotSelected(String color) {
        assertThat(
                multipleLabels.filter(new Locator.FilterOptions().setHasText(color))
        ).hasCount(0);
        return this;
    }

    public AutoCompletePage assertSingleColorSelected(String color) {
        assertThat(singleValue).hasText(color);
        return this;
    }

    private void selectFromAutocomplete(String expectedColor, boolean multipleField) {
        Locator optionByClass = page.locator(".auto-complete__option")
                .filter(new Locator.FilterOptions().setHasText(expectedColor))
                .first();

        Locator optionById = page.locator("[id*='option']")
                .filter(new Locator.FilterOptions().setHasText(expectedColor))
                .first();

        try {
            if (optionByClass.count() > 0) {
                click(optionByClass);
                assertSelection(expectedColor, multipleField);
                return;
            }

            if (optionById.count() > 0) {
                click(optionById);
                assertSelection(expectedColor, multipleField);
                return;
            }
        } catch (Exception ignored) {
        }

        page.keyboard().press("ArrowDown");
        page.keyboard().press("Enter");

        assertSelection(expectedColor, multipleField);
    }

    private void assertSelection(String color, boolean multipleField) {
        if (multipleField) {
            assertMultipleColorSelected(color);
        } else {
            assertSingleColorSelected(color);
        }
    }
}