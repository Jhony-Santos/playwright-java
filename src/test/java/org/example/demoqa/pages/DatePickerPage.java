package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DatePickerPage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/date-picker/?(\\?.*)?$");

    private final Locator selectDateInput;
    private final Locator dateAndTimeInput;

    // Select Date
    private final Locator simpleMonthSelect;
    private final Locator simpleYearSelect;

    // Date And Time
    private final Locator dateTimeMonthReadView;
    private final Locator dateTimeYearReadView;
    private final Locator previousMonthButton;
    private final Locator nextMonthButton;
    private final Locator timeListItems;

    public DatePickerPage(Page page) {
        super(page);

        this.selectDateInput = page.locator("#datePickerMonthYearInput");
        this.dateAndTimeInput = page.locator("#dateAndTimePickerInput");

        this.simpleMonthSelect = page.locator(".react-datepicker__month-select");
        this.simpleYearSelect = page.locator(".react-datepicker__year-select");

        this.dateTimeMonthReadView = page.locator(".react-datepicker__month-read-view--selected-month");
        this.dateTimeYearReadView = page.locator(".react-datepicker__year-read-view--selected-year");
        this.previousMonthButton = page.locator(".react-datepicker__navigation--previous");
        this.nextMonthButton = page.locator(".react-datepicker__navigation--next");
        this.timeListItems = page.locator(".react-datepicker__time-list-item");
    }

    public DatePickerPage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(selectDateInput).isVisible();
        assertThat(dateAndTimeInput).isVisible();
        return this;
    }

    public DatePickerPage selectDate(String month, String year, int day) {
        click(selectDateInput);

        assertThat(simpleMonthSelect).isVisible();
        assertThat(simpleYearSelect).isVisible();

        simpleMonthSelect.selectOption(month);
        simpleYearSelect.selectOption(year);
        stepDelay();

        clickDay(month, day, year);
        return this;
    }

    public DatePickerPage selectDateAndTime(String targetMonth, String targetYear, int targetDay, String targetTime24h) {
        click(dateAndTimeInput);

        assertThat(dateTimeMonthReadView).isVisible();
        assertThat(dateTimeYearReadView).isVisible();

        navigateDateTimeCalendarTo(targetMonth, targetYear);
        clickDay(targetMonth, targetDay, targetYear);
        clickTimeOption(targetTime24h);

        return this;
    }

    public DatePickerPage assertSelectDateValue(String expected) {
        assertThat(selectDateInput).hasValue(expected);
        return this;
    }

    public DatePickerPage assertDateAndTimeValue(String expected) {
        assertThat(dateAndTimeInput).hasValue(expected);
        return this;
    }

    private void navigateDateTimeCalendarTo(String targetMonth, String targetYear) {
        int guard = 0;

        while (guard < 60) {
            String currentMonth = safeText(dateTimeMonthReadView);
            String currentYear = safeText(dateTimeYearReadView);

            int cmp = compareMonthYear(currentMonth, currentYear, targetMonth, targetYear);
            if (cmp == 0) {
                return;
            }

            if (cmp < 0) {
                click(nextMonthButton);
            } else {
                click(previousMonthButton);
            }

            stepDelay();
            guard++;
        }

        throw new RuntimeException("Não foi possível navegar até " + targetMonth + " " + targetYear);
    }

    private void clickDay(String month, int day, String year) {
        String partialDate = month + " " + day + ordinal(day) + ", " + year;

        Locator dayLocator = page.locator(
                ".react-datepicker__day[aria-label*='" + partialDate + "']:not(.react-datepicker__day--outside-month)"
        ).first();

        assertThat(dayLocator).isVisible();
        click(dayLocator);
    }

    private void clickTimeOption(String time24h) {
        Locator timeOption = timeListItems
                .filter(new Locator.FilterOptions().setHasText(time24h))
                .first();

        assertThat(timeOption).isVisible();
        click(timeOption);
    }

    private String ordinal(int day) {
        if (day >= 11 && day <= 13) return "th";
        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private int compareMonthYear(String currentMonth, String currentYear, String targetMonth, String targetYear) {
        int currentY = Integer.parseInt(currentYear);
        int targetY = Integer.parseInt(targetYear);

        if (currentY != targetY) {
            return Integer.compare(currentY, targetY);
        }

        return Integer.compare(monthToNumber(currentMonth), monthToNumber(targetMonth));
    }

    private int monthToNumber(String month) {
        return switch (month.trim().toLowerCase()) {
            case "january" -> 1;
            case "february" -> 2;
            case "march" -> 3;
            case "april" -> 4;
            case "may" -> 5;
            case "june" -> 6;
            case "july" -> 7;
            case "august" -> 8;
            case "september" -> 9;
            case "october" -> 10;
            case "november" -> 11;
            case "december" -> 12;
            default -> throw new IllegalArgumentException("Mês inválido: " + month);
        };
    }

    private String safeText(Locator locator) {
        String text = locator.textContent();
        return text != null ? text.trim() : "";
    }
}