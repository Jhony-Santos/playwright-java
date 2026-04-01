package org.example.demoqa.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ResizablePage extends BasePage {

    private static final Pattern URL_REGEX = Pattern.compile(".*/resizable/?(\\?.*)?$");

    private final Locator restrictedBox;
    private final Locator restrictedHandle;

    private final Locator freeBox;
    private final Locator freeHandle;

    public ResizablePage(Page page) {
        super(page);

        this.restrictedBox = page.locator("#resizableBoxWithRestriction");
        this.restrictedHandle = restrictedBox.locator(".react-resizable-handle").first();

        this.freeBox = page.locator("#resizable");
        this.freeHandle = freeBox.locator(".react-resizable-handle").first();
    }

    public ResizablePage assertLoaded() {
        assertThat(page).hasURL(URL_REGEX);
        assertThat(restrictedBox).isVisible();
        assertThat(freeBox).isVisible();
        return this;
    }

    public ResizablePage resizeRestrictedBox(int deltaX, int deltaY) {
        dragResizeHandle(restrictedHandle, deltaX, deltaY);
        return this;
    }

    public ResizablePage resizeFreeBox(int deltaX, int deltaY) {
        dragResizeHandle(freeHandle, deltaX, deltaY);
        return this;
    }

    public ResizablePage assertRestrictedBoxSizeIncreased() {
        Size size = getElementSize(restrictedBox);
        if (size.width <= 200 || size.height <= 200) {
            throw new AssertionError("A caixa com restricao nao aumentou. Atual: " + size);
        }
        return this;
    }

    public ResizablePage assertFreeBoxSizeIncreased() {
        Size size = getElementSize(freeBox);
        if (size.width <= 200 || size.height <= 200) {
            throw new AssertionError("A caixa sem restricao nao aumentou. Atual: " + size);
        }
        return this;
    }

    public ResizablePage assertRestrictedBoxWithinLimits() {
        Size size = getElementSize(restrictedBox);

        if (size.width < 150 || size.width > 500) {
            throw new AssertionError("Largura fora do limite da caixa com restricao: " + size.width);
        }

        if (size.height < 150 || size.height > 300) {
            throw new AssertionError("Altura fora do limite da caixa com restricao: " + size.height);
        }

        return this;
    }

    public Size getRestrictedBoxSize() {
        return getElementSize(restrictedBox);
    }

    public Size getFreeBoxSize() {
        return getElementSize(freeBox);
    }

    private void dragResizeHandle(Locator handle, int deltaX, int deltaY) {
        removeObstructionsSafe();
        handle.scrollIntoViewIfNeeded();

        BoundingBox box = handle.boundingBox();
        if (box == null) {
            throw new RuntimeException("Nao foi possivel obter o bounding box do handle de resize.");
        }

        double startX = box.x + (box.width / 2.0);
        double startY = box.y + (box.height / 2.0);

        double endX = startX + deltaX;
        double endY = startY + deltaY;

        page.mouse().move(startX, startY);
        page.waitForTimeout(100);
        page.mouse().down();
        page.waitForTimeout(100);

        int steps = 12;
        for (int i = 1; i <= steps; i++) {
            double x = startX + (endX - startX) * i / steps;
            double y = startY + (endY - startY) * i / steps;
            page.mouse().move(x, y);
            page.waitForTimeout(20);
        }

        page.waitForTimeout(100);
        page.mouse().up();
        page.waitForTimeout(300);
    }

    private Size getElementSize(Locator locator) {
        String widthText = locator.evaluate("el => getComputedStyle(el).width").toString();
        String heightText = locator.evaluate("el => getComputedStyle(el).height").toString();

        int width = parsePixels(widthText);
        int height = parsePixels(heightText);

        return new Size(width, height);
    }

    private int parsePixels(String value) {
        return (int) Math.round(Double.parseDouble(value.replace("px", "").trim()));
    }

    public record Size(int width, int height) {
        @Override
        public String toString() {
            return width + "x" + height;
        }
    }
}