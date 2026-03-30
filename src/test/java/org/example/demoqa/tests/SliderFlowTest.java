package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.data.SliderDataFactory;
import org.example.demoqa.data.models.SliderData;
import org.example.demoqa.pages.HomePage;
import org.example.demoqa.pages.SliderPage;
import org.junit.jupiter.api.Test;

public class SliderFlowTest extends BaseTest {

    @Test
    void shouldValidateInitialSliderValue() {
        SliderPage sliderPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openSlider();

        sliderPage.assertCurrentValue("25");
    }

    @Test
    void shouldMoveSliderTo50() {
        SliderData data = SliderDataFactory.to50();

        SliderPage sliderPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openSlider();

        sliderPage
                .moveToValue(data.targetValue())
                .assertValue(data.targetValue());
    }

    @Test
    void shouldMoveSliderTo75() {
        SliderData data = SliderDataFactory.to75();

        SliderPage sliderPage = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openSlider();

        sliderPage
                .moveToValue(data.targetValue())
                .assertValue(data.targetValue());
    }
}