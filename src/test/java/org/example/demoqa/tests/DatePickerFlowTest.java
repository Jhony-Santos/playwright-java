package org.example.demoqa.tests;

import org.example.demoqa.BaseTest;
import org.example.demoqa.data.DatePickerDataFactory;
import org.example.demoqa.data.models.DatePickerData;
import org.example.demoqa.pages.DatePickerPage;
import org.example.demoqa.pages.HomePage;
import org.junit.jupiter.api.Test;

public class DatePickerFlowTest extends BaseTest {

    @Test
    void shouldSelectSimpleDate() {
        DatePickerData data = DatePickerDataFactory.valid();

        DatePickerPage datePicker = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openDatePicker();

        datePicker
                .selectDate(data.selectDateMonth(), data.selectDateYear(), data.selectDateDay())
                .assertSelectDateValue(data.selectDateExpectedValue());
    }

    @Test
    void shouldSelectDateAndTime() {
        DatePickerData data = DatePickerDataFactory.valid();

        DatePickerPage datePicker = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openDatePicker();

        datePicker
                .selectDateAndTime(
                        data.dateTimeMonth(),
                        data.dateTimeYear(),
                        data.dateTimeDay(),
                        data.dateTimeTime()
                )
                .assertDateAndTimeValue(data.dateTimeExpectedValue());
    }

    @Test
    void shouldSelectAnotherDateAndDateTime() {
        DatePickerData data = DatePickerDataFactory.anotherValid();

        DatePickerPage datePicker = new HomePage(page)
                .gotoHome()
                .openWidgets()
                .openDatePicker();

        datePicker
                .selectDate(data.selectDateMonth(), data.selectDateYear(), data.selectDateDay())
                .assertSelectDateValue(data.selectDateExpectedValue())
                .selectDateAndTime(
                        data.dateTimeMonth(),
                        data.dateTimeYear(),
                        data.dateTimeDay(),
                        data.dateTimeTime()
                )
                .assertDateAndTimeValue(data.dateTimeExpectedValue());
    }
}