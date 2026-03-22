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
}