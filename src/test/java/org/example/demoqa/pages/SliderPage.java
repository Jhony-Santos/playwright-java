package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SliderPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/slider/?(\\?.*)?$");

    private final Locator slider;
    private final Locator sliderValueInput;

    public SliderPage(Page page) {
        super(page);
        this.slider = page.locator("#slider");
        this.sliderValueInput = page.locator("#sliderValue");
    }

    public SliderPage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(slider).isVisible();
        assertThat(sliderValueInput).isVisible();
        return this;
    }

    public SliderPage assertCurrentValue(String expectedValue) {
        assertThat(slider).hasValue(expectedValue);
        assertThat(sliderValueInput).hasValue(expectedValue);
        return this;
    }

    public SliderPage moveToValue(int targetValue) {
        int currentValue = Integer.parseInt(slider.inputValue());
        int diff = targetValue - currentValue;

        slider.focus();
        stepDelay();

        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                page.keyboard().press("ArrowRight");
            }
        } else if (diff < 0) {
            for (int i = 0; i < Math.abs(diff); i++) {
                page.keyboard().press("ArrowLeft");
            }
        }

        return this;
    }

    public SliderPage assertValue(int expectedValue) {
        String expected = String.valueOf(expectedValue);
        assertThat(slider).hasValue(expected);
        assertThat(sliderValueInput).hasValue(expected);
        return this;
    }
}