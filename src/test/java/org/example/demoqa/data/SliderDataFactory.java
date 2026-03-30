package org.example.demoqa.data;

import org.example.demoqa.data.models.SliderData;

public final class SliderDataFactory {
    private SliderDataFactory() {}

    public static SliderData to25() {
        return new SliderData(25);
    }

    public static SliderData to50() {
        return new SliderData(50);
    }

    public static SliderData to75() {
        return new SliderData(75);
    }
}