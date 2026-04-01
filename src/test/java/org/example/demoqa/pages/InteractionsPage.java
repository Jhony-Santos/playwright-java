package org.example.demoqa.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import java.util.List;

public class InteractionsPage extends BasePage {

    public InteractionsPage(Page page) {
        super(page);
    }

    public SortablePage openSortable() {
        page.navigate("https://demoqa.com/sortable",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/sortable", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(List.of("body", "#demo-tab-list", "#demo-tab-grid"), 60_000, true);

        return new SortablePage(page).assertLoaded();
    }



    public SelectablePage openSelectable() {
        page.navigate("https://demoqa.com/selectable",
                new Page.NavigateOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/selectable", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(java.util.List.of("body", "#demo-tab-list", "#demo-tab-grid"), 60_000, true);

        return new SelectablePage(page).assertLoaded();
    }


    public ResizablePage openResizable() {
        page.navigate("https://demoqa.com/resizable",
                new Page.NavigateOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/resizable", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(java.util.List.of("body", "#resizableBoxWithRestriction", "#resizable"), 60_000, true);

        return new ResizablePage(page).assertLoaded();
    }


    public DroppablePage openDroppable() {
        page.navigate("https://demoqa.com/droppable",
                new Page.NavigateOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
        page.waitForURL("**/droppable", new Page.WaitForURLOptions().setTimeout(30_000));

        safeRemoveObstructions();
        ensureAppIsUp(java.util.List.of("body", "#droppable"), 60_000, true);

        return new DroppablePage(page).assertLoaded();
    }







}