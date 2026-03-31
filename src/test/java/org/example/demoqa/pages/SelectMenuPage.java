package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SelectMenuPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/select-menu/?(\\?.*)?$");

    private final Locator selectValueInput;
    private final Locator selectOneInput;
    private final Locator oldStyleSelect;
    private final Locator multiSelectDropDownInput;
    private final Locator standardMultiSelect;

    public SelectMenuPage(Page page) {
        super(page);

        this.selectValueInput = page.locator("#react-select-2-input");
        this.selectOneInput = page.locator("#react-select-3-input");
        this.oldStyleSelect = page.locator("#oldSelectMenu");
        this.multiSelectDropDownInput = page.locator("#react-select-4-input");
        this.standardMultiSelect = page.locator("#cars");
    }

    public SelectMenuPage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(selectValueInput).isVisible();
        assertThat(selectOneInput).isVisible();
        assertThat(oldStyleSelect).isVisible();
        assertThat(multiSelectDropDownInput).isVisible();
        assertThat(standardMultiSelect).isVisible();
        return this;
    }

    public SelectMenuPage selectValue(String value) {
        selectReactOption(selectValueInput, value);
        return this;
    }

    public SelectMenuPage selectOne(String value) {
        selectReactOption(selectOneInput, value);
        return this;
    }

    public SelectMenuPage selectOldStyleColor(String visibleText) {
        oldStyleSelect.selectOption(new SelectOption().setLabel(visibleText));
        return this;
    }

    public SelectMenuPage selectMultiSelectDropDown(List<String> values) {
        for (String value : values) {
            selectReactOption(multiSelectDropDownInput, value);
        }
        return this;
    }

    public SelectMenuPage selectStandardMulti(List<String> values) {
        standardMultiSelect.selectOption(values.toArray(new String[0]));
        return this;
    }

    public SelectMenuPage assertSelectValueSelected(String value) {
        Locator selectedValue = page.locator("[class*='singleValue']")
                .filter(new Locator.FilterOptions().setHasText(value))
                .first();

        assertThat(selectedValue).isVisible();
        return this;
    }

    public SelectMenuPage assertSelectOneSelected(String value) {
        Locator selectedValue = page.locator("[class*='singleValue']")
                .filter(new Locator.FilterOptions().setHasText(value))
                .first();

        assertThat(selectedValue).isVisible();
        return this;
    }

    public SelectMenuPage assertOldStyleColorSelected(String expectedLabel) {
        assertThat(oldStyleSelect).hasValue(resolveOldStyleValue(expectedLabel));
        return this;
    }

    public SelectMenuPage assertMultiSelectDropDownSelected(List<String> values) {
        for (String value : values) {
            Locator selectedValue = page.locator("[class*='multi-value'], [class*='multiValue']")
                    .filter(new Locator.FilterOptions().setHasText(value))
                    .first();

            assertThat(selectedValue).isVisible();
        }
        return this;
    }

    public SelectMenuPage assertStandardMultiSelected(List<String> expectedValues) {
        Object result = standardMultiSelect.evaluate(
                "el => Array.from(el.selectedOptions).map(o => o.value)"
        );

        String actual = String.valueOf(result);
        for (String expected : expectedValues) {
            if (!actual.contains(expected)) {
                throw new AssertionError(
                        "Valor esperado não selecionado no multi select padrão: " + expected + ". Atual=" + actual
                );
            }
        }
        return this;
    }

    private void selectReactOption(Locator input, String value) {
        removeObstructionsSafe();
        input.scrollIntoViewIfNeeded();
        input.click();
        input.fill(value);
        page.waitForTimeout(250);

        Locator option = page.locator("[id*='option']")
                .filter(new Locator.FilterOptions().setHasText(value))
                .first();

        try {
            if (option.count() > 0) {
                option.click();
                return;
            }
        } catch (Exception ignored) {
        }

        page.keyboard().press("ArrowDown");
        page.keyboard().press("Enter");
    }

    private String resolveOldStyleValue(String visibleText) {
        return switch (visibleText.toLowerCase()) {
            case "red" -> "red";
            case "blue" -> "1";
            case "green" -> "2";
            case "yellow" -> "3";
            case "purple" -> "4";
            case "black" -> "5";
            case "white" -> "6";
            case "voilet" -> "7";
            case "indigo" -> "8";
            case "magenta" -> "9";
            case "aqua" -> "10";
            default -> throw new IllegalArgumentException("Cor inválida: " + visibleText);
        };
    }
}