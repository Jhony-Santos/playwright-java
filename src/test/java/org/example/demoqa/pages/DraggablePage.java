package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DraggablePage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/dragabble/?(\\?.*)?$");

    private final Locator simpleTab;
    private final Locator axisRestrictedTab;
    private final Locator containerRestrictedTab;
    private final Locator cursorStyleTab;

    private final Locator simpleDragBox;
    private final Locator axisXBox;
    private final Locator axisYBox;

    private final Locator containerRestrictionPane;
    private final Locator containmentWrapper;
    private final Locator containedBox;
    private final Locator containedParent;
    private final Locator containedParentWrapper;


    private final Locator cursorCenterBox;
    private final Locator cursorTopLeftBox;
    private final Locator cursorBottomBox;




    public DraggablePage(Page page) {
        super(page);

        this.simpleTab = page.locator("#draggableExample-tab-simple");
        this.axisRestrictedTab = page.locator("#draggableExample-tab-axisRestriction");
        this.containerRestrictedTab = page.locator("#draggableExample-tab-containerRestriction");
        this.cursorStyleTab = page.locator("#draggableExample-tab-cursorStyle");

        this.simpleDragBox = page.locator("#dragBox");
        this.axisXBox = page.locator("#restrictedX");
        this.axisYBox = page.locator("#restrictedY");

        this.containerRestrictionPane = page.locator("#draggableExample-tabpane-containerRestriction");
        this.containmentWrapper = page.locator("#containmentWrapper");

        this.containedBox = page.locator("#containmentWrapper .draggable");

        this.containedParent = page.locator(
                "#draggableExample-tabpane-containerRestriction span.ui-draggable"
        );

        this.containedParentWrapper = page.locator(
                "#draggableExample-tabpane-containerRestriction span.ui-draggable >> xpath=.."
        );


        this.cursorCenterBox = page.locator("#cursorCenter").first();
        this.cursorTopLeftBox = page.locator("#cursorTopLeft");
        this.cursorBottomBox = page.locator("#cursorBottom");




    }

    public DraggablePage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(simpleTab).isVisible();
        assertThat(axisRestrictedTab).isVisible();
        assertThat(containerRestrictedTab).isVisible();
        assertThat(cursorStyleTab).isVisible();
        assertThat(simpleDragBox).isVisible();
        return this;
    }

    public DraggablePage openSimpleTab() {
        click(simpleTab);
        assertThat(simpleTab).hasAttribute("aria-selected", "true");
        assertThat(simpleDragBox).isVisible();
        return this;
    }

    public DraggablePage dragSimpleBoxBy(int deltaX, int deltaY) {
        openSimpleTab();

        BoundingBox before = getBoundingBox(simpleDragBox);
        dragBy(simpleDragBox, deltaX, deltaY);
        BoundingBox after = getBoundingBox(simpleDragBox);

        assertElementMoved(before, after, "simpleDragBox");
        return this;
    }

    public DraggablePage assertSimpleBoxMovedFromInitialPosition() {
        String style = simpleDragBox.getAttribute("style");

        if (style == null || (!style.contains("left:") && !style.contains("top:"))) {
            throw new AssertionError("O elemento nao parece ter sido movido. style=" + style);
        }

        return this;
    }

    public DraggablePage openAxisRestrictedTab() {
        click(axisRestrictedTab);
        assertThat(axisRestrictedTab).hasAttribute("aria-selected", "true");
        assertThat(axisXBox).isVisible();
        assertThat(axisYBox).isVisible();
        return this;
    }

    public DraggablePage dragXAxisBox(int deltaX, int deltaY) {
        openAxisRestrictedTab();
        dragBy(axisXBox, deltaX, deltaY);
        return this;
    }

    public DraggablePage dragYAxisBox(int deltaX, int deltaY) {
        openAxisRestrictedTab();
        dragBy(axisYBox, deltaX, deltaY);
        return this;
    }

    public DraggablePage assertMovedOnlyHorizontally() {
        String style = axisXBox.getAttribute("style");

        int left = extractPx(style, "left");
        int top = extractPx(style, "top");

        if (left == 0 || top != 0) {
            throw new AssertionError(
                    "Elemento X nao respeitou restricao horizontal. " +
                            "Esperado: left != 0 e top = 0. " +
                            "Atual: left=" + left + ", top=" + top + ", style=" + style
            );
        }

        return this;
    }

    public DraggablePage assertMovedOnlyVertically() {
        String style = axisYBox.getAttribute("style");

        int left = extractPx(style, "left");
        int top = extractPx(style, "top");

        if (top == 0 || left != 0) {
            throw new AssertionError(
                    "Elemento Y nao respeitou restricao vertical. " +
                            "Esperado: top != 0 e left = 0. " +
                            "Atual: left=" + left + ", top=" + top + ", style=" + style
            );
        }

        return this;
    }

    public DraggablePage openContainerRestrictedTab() {
        click(containerRestrictedTab);

        assertThat(containerRestrictedTab).hasAttribute("aria-selected", "true");
        assertThat(containerRestrictionPane).isVisible();

        assertThat(containmentWrapper).isVisible();
        assertThat(containedBox).isVisible();

        assertThat(containedParentWrapper).isVisible();
        assertThat(containedParent).isVisible();

        return this;
    }

    public DraggablePage dragContainedBox(int deltaX, int deltaY) {
        openContainerRestrictedTab();

        BoundingBox before = getBoundingBox(containedBox);
        dragBy(containedBox, deltaX, deltaY);
        BoundingBox after = getBoundingBox(containedBox);

        assertElementMoved(before, after, "containedBox");
        return this;
    }



    public DraggablePage dragContainedParent(int deltaX, int deltaY) {
        openContainerRestrictedTab();

        BoundingBox before = getBoundingBox(containedParent);

        dragByFromTopLeft(containedParent, deltaX, deltaY);

        BoundingBox after = getBoundingBox(containedParent);

        assertElementMoved(before, after, "containedParent");
        return this;
    }


    private void dragByFromTopLeft(Locator source, int deltaX, int deltaY) {
        removeObstructionsSafe();

        source.scrollIntoViewIfNeeded();
        page.waitForTimeout(200);

        BoundingBox box = getBoundingBox(source);

        double startX = box.x + 5;
        double startY = box.y + 5;

        double endX = startX + deltaX;
        double endY = startY + deltaY;

        page.mouse().move(startX, startY);
        page.waitForTimeout(100);

        page.mouse().down();
        page.waitForTimeout(150);

        int steps = 25;
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





    public DraggablePage assertContainedBoxMovedInsideContainer() {
        assertElementInsideContainer(containedBox, containmentWrapper);
        return this;
    }

    public DraggablePage assertContainedParentMovedInsideParent() {
        assertElementInsideContainer(containedParent, containedParentWrapper);
        return this;
    }

    private void dragBy(Locator source, int deltaX, int deltaY) {
        removeObstructionsSafe();

        source.scrollIntoViewIfNeeded();
        page.waitForTimeout(200);

        BoundingBox box = getBoundingBox(source);

        double startX = box.x + box.width / 2.0;
        double startY = box.y + box.height / 2.0;

        double endX = startX + deltaX;
        double endY = startY + deltaY;

        page.mouse().move(startX, startY);
        page.waitForTimeout(100);

        page.mouse().down();
        page.waitForTimeout(150);

        int steps = 25;
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

    private BoundingBox getBoundingBox(Locator locator) {
        BoundingBox box = locator.boundingBox();

        if (box == null) {
            throw new AssertionError("Nao foi possivel obter bounding box do elemento.");
        }

        return box;
    }

    private void assertElementMoved(BoundingBox before, BoundingBox after, String elementName) {
        boolean moved = Math.abs(before.x - after.x) > 1 || Math.abs(before.y - after.y) > 1;

        if (!moved) {
            throw new AssertionError(
                    "Elemento nao foi movido: " + elementName +
                            ". Antes=" + before +
                            ", Depois=" + after
            );
        }
    }

    private void assertElementInsideContainer(Locator element, Locator container) {
        BoundingBox elementBox = getBoundingBox(element);
        BoundingBox containerBox = getBoundingBox(container);

        double tolerance = 1.0;

        boolean inside =
                elementBox.x + tolerance >= containerBox.x &&
                        elementBox.y + tolerance >= containerBox.y &&
                        elementBox.x + elementBox.width <= containerBox.x + containerBox.width + tolerance &&
                        elementBox.y + elementBox.height <= containerBox.y + containerBox.height + tolerance;

        if (!inside) {
            throw new AssertionError(
                    "Elemento saiu dos limites. Elemento=" + elementBox + ", Container=" + containerBox
            );
        }
    }

    private int extractPx(String style, String property) {
        if (style == null) {
            return 0;
        }

        var matcher = Pattern
                .compile(property + ":\\s*(-?\\d+)px")
                .matcher(style);

        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }



    public DraggablePage openCursorStyleTab() {
        click(cursorStyleTab);

        assertThat(cursorStyleTab).hasAttribute("aria-selected", "true");

        assertThat(cursorCenterBox).isVisible();
        assertThat(cursorTopLeftBox).isVisible();
        assertThat(cursorBottomBox).isVisible();

        return this;
    }



    private void dragFromCustomPoint(Locator source, double offsetX, double offsetY, int deltaX, int deltaY) {
        removeObstructionsSafe();

        source.scrollIntoViewIfNeeded();
        page.waitForTimeout(200);

        BoundingBox box = getBoundingBox(source);

        double startX = box.x + offsetX;
        double startY = box.y + offsetY;

        double endX = startX + deltaX;
        double endY = startY + deltaY;

        page.mouse().move(startX, startY);
        page.waitForTimeout(100);

        page.mouse().down();
        page.waitForTimeout(150);

        int steps = 25;
        for (int i = 1; i <= steps; i++) {
            double x = startX + (endX - startX) * i / steps;
            double y = startY + (endY - startY) * i / steps;
            page.mouse().move(x, y);
            page.waitForTimeout(20);
        }

        page.waitForTimeout(100);
        page.mouse().up();
        page.waitForTimeout(400);
    }


    public DraggablePage dragCursorCenterBox(int dx, int dy) {
        openCursorStyleTab();

        BoundingBox before = getBoundingBox(cursorCenterBox);

        dragBy(cursorCenterBox, dx, dy);

        BoundingBox after = getBoundingBox(cursorCenterBox);

        assertElementMoved(before, after, "cursorCenterBox");

        return this;
    }


    public DraggablePage dragCursorTopLeftBox(int dx, int dy) {
        openCursorStyleTab();

        BoundingBox before = getBoundingBox(cursorTopLeftBox);

        dragFromCustomPoint(cursorTopLeftBox, 5, 5, dx, dy);

        BoundingBox after = getBoundingBox(cursorTopLeftBox);

        assertElementMoved(before, after, "cursorTopLeftBox");

        return this;
    }


    public DraggablePage dragCursorBottomBox(int dx, int dy) {
        openCursorStyleTab();

        BoundingBox box = getBoundingBox(cursorBottomBox);
        BoundingBox before = box;

        dragFromCustomPoint(cursorBottomBox, box.width / 2, box.height - 5, dx, dy);

        BoundingBox after = getBoundingBox(cursorBottomBox);

        assertElementMoved(before, after, "cursorBottomBox");

        return this;
    }
}