package org.example.demoqa.pages;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import java.util.List;

public class WidgetsPage extends BasePage {

    public WidgetsPage(Page page) {
        super(page);
    }

    public AccordionPage openAccordion() {
        page.navigate("https://demoqa.com/accordian",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/accordian", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body"), 60_000, true);

        return new AccordionPage(page);
    }

    public AutoCompletePage openAutoComplete() {
        page.navigate("https://demoqa.com/auto-complete",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/auto-complete", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#autoCompleteMultipleInput", "#autoCompleteSingleInput"), 60_000, true);

        return new AutoCompletePage(page).assertLoaded();
    }


    public DatePickerPage openDatePicker() {
        page.navigate("https://demoqa.com/date-picker",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/date-picker", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#datePickerMonthYearInput", "#dateAndTimePickerInput"), 60_000, true);

        return new DatePickerPage(page).assertLoaded();
    }

    public SliderPage openSlider() {
        page.navigate("https://demoqa.com/slider",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/slider", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#slider", "#sliderValue"), 60_000, true);

        return new SliderPage(page).assertLoaded();
    }

    public ProgressBarPage openProgressBar() {
        page.navigate("https://demoqa.com/progress-bar",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/progress-bar", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#progressBar", "#startStopButton"), 60_000, true);

        return new ProgressBarPage(page).assertLoaded();
    }

    public TabsPage openTabs() {
        page.navigate("https://demoqa.com/tabs",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/tabs", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#demo-tab-what", "#demo-tab-origin", "#demo-tab-use", "#demo-tab-more"), 60_000, true);

        return new TabsPage(page).assertLoaded();
    }



}