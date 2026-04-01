package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DroppablePage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/droppable/?(\\?.*)?$");

    private final Locator simpleTab;
    private final Locator acceptTab;
    private final Locator preventPropagationTab;
    private final Locator revertDraggableTab;

    private final Locator simplePane;
    private final Locator acceptPane;
    private final Locator revertPane;

    private final Locator simpleDrag;
    private final Locator simpleDrop;

    private final Locator acceptableDrag;
    private final Locator notAcceptableDrag;
    private final Locator acceptDrop;

    private final Locator revertableDrag;
    private final Locator notRevertableDrag;
    private final Locator revertDrop;

    public DroppablePage(Page page) {
        super(page);

        this.simpleTab = page.locator("#droppableExample-tab-simple");
        this.acceptTab = page.locator("#droppableExample-tab-accept");
        this.preventPropagationTab = page.locator("#droppableExample-tab-preventPropogation");
        this.revertDraggableTab = page.locator("#droppableExample-tab-revertable");

        this.simplePane = page.locator("#droppableExample-tabpane-simple");
        this.acceptPane = page.locator("#droppableExample-tabpane-accept");
        this.revertPane = page.locator("#droppableExample-tabpane-revertable");

        this.simpleDrag = page.locator("#draggable");
        this.simpleDrop = simplePane.locator("#droppable");

        this.acceptableDrag = page.locator("#acceptable");

        Locator notAcceptableById = page.locator("#notAcceptable");
        this.notAcceptableDrag = notAcceptableById.count() > 0
                ? notAcceptableById
                : acceptPane.locator(".drag-box").filter(
                new Locator.FilterOptions().setHasText("Not Acceptable")
        ).first();

        // Não depender de id de container externo.
        // Pega a drop-box dentro da pane Accept.
        this.acceptDrop = acceptPane.locator(".drop-box").first();

        this.revertableDrag = page.locator("#revertable");
        this.notRevertableDrag = page.locator("#notRevertable");
        this.revertDrop = revertPane.locator("#droppable");
    }

    public DroppablePage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(simpleTab).isVisible();
        assertThat(acceptTab).isVisible();
        assertThat(preventPropagationTab).isVisible();
        assertThat(revertDraggableTab).isVisible();
        return this;
    }

    public DroppablePage openSimpleTab() {
        click(simpleTab);
        assertThat(simpleTab).hasAttribute("aria-selected", "true");
        assertThat(simplePane).isVisible();
        assertThat(simpleDrag).isVisible();
        assertThat(simpleDrop).isVisible();
        return this;
    }

    public DroppablePage openAcceptTab() {
        click(acceptTab);
        assertThat(acceptTab).hasAttribute("aria-selected", "true");
        assertThat(acceptPane).isVisible();
        assertThat(acceptableDrag).isVisible();
        assertThat(acceptDrop).isVisible();
        return this;
    }

    public DroppablePage openRevertDraggableTab() {
        click(revertDraggableTab);
        assertThat(revertDraggableTab).hasAttribute("aria-selected", "true");
        assertThat(revertPane).isVisible();
        assertThat(revertableDrag).isVisible();
        assertThat(revertDrop).isVisible();
        return this;
    }

    public DroppablePage dragSimpleToDrop() {
        openSimpleTab();
        dragAndDrop(simpleDrag, simpleDrop);
        return this;
    }

    public DroppablePage dragAcceptableToDrop() {
        openAcceptTab();
        dragAndDrop(acceptableDrag, acceptDrop);
        return this;
    }

    public DroppablePage dragNotAcceptableToDrop() {
        openAcceptTab();
        dragAndDrop(notAcceptableDrag, acceptDrop);
        return this;
    }

    public DroppablePage dragRevertableToDrop() {
        openRevertDraggableTab();
        dragAndDrop(revertableDrag, revertDrop);
        return this;
    }

    public DroppablePage dragNotRevertableToDrop() {
        openRevertDraggableTab();
        dragAndDrop(notRevertableDrag, revertDrop);
        return this;
    }

    public DroppablePage assertSimpleDropped() {
        assertThat(simpleDrop).containsText("Dropped!");
        return this;
    }

    public DroppablePage assertAcceptableDropped() {
        assertThat(acceptDrop).containsText("Dropped!");
        return this;
    }

    public DroppablePage assertNotAcceptableDidNotDrop() {
        assertThat(acceptDrop).containsText("Drop here");
        return this;
    }

    public DroppablePage assertNotRevertableStayedDropped() {
        assertThat(revertDrop).containsText("Dropped!");
        return this;
    }

    private void dragAndDrop(Locator source, Locator target) {
        removeObstructionsSafe();

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

        double endX = targetBox.x + (targetBox.width / 2.0);
        double endY = targetBox.y + (targetBox.height / 2.0);

        page.mouse().move(startX, startY);
        page.waitForTimeout(100);
        page.mouse().down();
        page.waitForTimeout(150);

        int steps = 15;
        for (int i = 1; i <= steps; i++) {
            double x = startX + (endX - startX) * i / steps;
            double y = startY + (endY - startY) * i / steps;
            page.mouse().move(x, y);
            page.waitForTimeout(20);
        }

        page.waitForTimeout(100);
        page.mouse().up();
        page.waitForTimeout(500);
    }
}