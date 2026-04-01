package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SortablePage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/sortable/?(\\?.*)?$");

    private final Locator listTab;
    private final Locator gridTab;

    private final Locator listContainer;
    private final Locator listItems;

    private final Locator gridContainer;
    private final Locator gridItems;

    public SortablePage(Page page) {
        super(page);

        this.listTab = page.locator("#demo-tab-list");
        this.gridTab = page.locator("#demo-tab-grid");

        this.listContainer = page.locator(".vertical-list-container");
        this.listItems = listContainer.locator(".list-group-item");

        this.gridContainer = page.locator(".create-grid");
        this.gridItems = gridContainer.locator(".list-group-item");
    }

    public SortablePage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(listTab).isVisible();
        assertThat(gridTab).isVisible();
        return this;
    }

    public SortablePage openListTab() {
        click(listTab);
        assertThat(listTab).hasAttribute("aria-selected", "true");
        assertThat(listContainer).isVisible();
        return this;
    }

    public SortablePage openGridTab() {
        click(gridTab);
        assertThat(gridTab).hasAttribute("aria-selected", "true");
        assertThat(gridContainer).isVisible();
        return this;
    }

    public SortablePage dragListItemToEnd(String sourceText, String targetText) {
        openListTab();
        dragItemByText(listItems, sourceText, targetText, DropPosition.BELOW);
        return this;
    }

    public SortablePage dragGridItemToEnd(String sourceText, String targetText) {
        openGridTab();
        dragItemByText(gridItems, sourceText, targetText, DropPosition.BELOW_RIGHT);
        return this;
    }

    public SortablePage assertListOrder(List<String> expectedOrder) {
        List<String> actual = textsOf(listItems);
        if (!actual.equals(expectedOrder)) {
            throw new AssertionError(
                    "Ordem da lista diferente.\nEsperado: " + expectedOrder + "\nAtual: " + actual
            );
        }
        return this;
    }

    public SortablePage assertGridOrder(List<String> expectedOrder) {
        List<String> actual = textsOf(gridItems);
        if (!actual.equals(expectedOrder)) {
            throw new AssertionError(
                    "Ordem do grid diferente.\nEsperado: " + expectedOrder + "\nAtual: " + actual
            );
        }
        return this;
    }

    private void dragItemByText(Locator items, String sourceText, String targetText, DropPosition dropPosition) {
        List<String> before = textsOf(items);

        Locator source = items.filter(new Locator.FilterOptions().setHasText(sourceText)).first();
        Locator target = items.filter(new Locator.FilterOptions().setHasText(targetText)).first();

        source.scrollIntoViewIfNeeded();
        target.scrollIntoViewIfNeeded();
        page.waitForTimeout(200);

        BoundingBox sourceBox = source.boundingBox();
        BoundingBox targetBox = target.boundingBox();

        if (sourceBox == null || targetBox == null) {
            throw new RuntimeException("Nao foi possivel obter as coordenadas do drag and drop.");
        }

        double startX = sourceBox.x + (sourceBox.width / 2.0);
        double startY = sourceBox.y + (sourceBox.height / 2.0);

        double endX;
        double endY;

        switch (dropPosition) {
            case BELOW:
                endX = targetBox.x + (targetBox.width / 2.0);
                endY = targetBox.y + targetBox.height - 2;
                break;
            case BELOW_RIGHT:
                endX = targetBox.x + targetBox.width - 6;
                endY = targetBox.y + targetBox.height - 6;
                break;
            default:
                throw new IllegalStateException("Posicao de drop nao suportada.");
        }

        page.mouse().move(startX, startY);
        page.waitForTimeout(100);
        page.mouse().down();
        page.waitForTimeout(150);

        int steps = 20;
        for (int i = 1; i <= steps; i++) {
            double x = startX + (endX - startX) * i / steps;
            double y = startY + (endY - startY) * i / steps;
            page.mouse().move(x, y);
            page.waitForTimeout(25);
        }

        page.waitForTimeout(150);
        page.mouse().up();
        page.waitForTimeout(700);

        List<String> after = textsOf(items);
        if (after.equals(before)) {
            throw new AssertionError(
                    "O drag and drop nao alterou a ordem.\nAntes: " + before + "\nDepois: " + after
            );
        }
    }

    private List<String> textsOf(Locator items) {
        int count = items.count();
        List<String> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String text = items.nth(i).textContent();
            values.add(text != null ? text.trim() : "");
        }

        return values;
    }

    private enum DropPosition {
        BELOW,
        BELOW_RIGHT
    }
}