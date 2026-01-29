package foundationgames.enhancedblockentities.config.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;

import java.util.ArrayList;
import java.util.List;

public class WidgetRowListWidget extends ContainerObjectSelectionList<WidgetRowListWidget.Entry> {
    public static final int SPACING = 3;

    public final int rowWidth;
    public final int rowHeight;

    public WidgetRowListWidget(Minecraft mc, int w, int h, int y, int rowWidth, int rowHeight) {
        super(mc, w, h, y, rowHeight + SPACING);
        this.rowWidth = rowWidth;
        this.rowHeight = rowHeight;
    }

    public void add(AbstractWidget... widgets) {
        if (widgets.length == 0) return;

        var grid = new GridLayout();
        grid.columnSpacing(SPACING);
        var adder = grid.createRowHelper(widgets.length);

        int width = (this.rowWidth - ((widgets.length - 1) * SPACING)) / widgets.length;

        for (var widget : widgets) {
            widget.setWidth(width);
            widget.setHeight(this.rowHeight);
            adder.addChild(widget);
        }

        grid.arrangeElements();

        this.addEntry(new Entry(grid));
    }

    @Override
    public int getRowWidth() {
        return rowWidth;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width - 6;
    }

    @Override
    protected void renderListBackground(GuiGraphics graphics) {

    }

    public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        private final GridLayout widget;
        private final List<AbstractWidget> children = new ArrayList<>();

        public Entry(GridLayout widget) {
            this.widget = widget;
            widget.visitWidgets(children::add);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.widget.setPosition(x - 3, y);
            this.widget.arrangeElements();

            this.widget.visitWidgets(c -> c.render(graphics, mouseX, mouseY, tickDelta));
        }
    }
}