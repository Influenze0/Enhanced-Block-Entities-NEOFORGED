package foundationgames.enhancedblockentities.config.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.network.chat.Component;

public class SectionTextWidget extends AbstractStringWidget {
    public SectionTextWidget(Component message, Font font) {
        this(0, 0, 200, 20, message, font);
    }

    public SectionTextWidget(int x, int y, int width, int height, Component message, Font font) {
        super(x, y, width, height, message, font);
        this.active = false;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        final int white = 0xFFFFFFFF;
        var font = this.getFont();
        var msg = this.getMessage();

        int l = this.getX();
        int w = this.getWidth();
        int r = l + w;
        int y = (this.getY() + this.getHeight()) - 6;

        int tx = l + (w / 2);
        int ty = y - (font.lineHeight / 2);
        int tw = font.width(msg);

        int ml = l + ((w - tw) / 2) - 5;
        int mr = ml + tw + 10;

        l += 1;
        r -= 1;

        guiGraphics.fill(l, y, ml, y + 2, white);
        guiGraphics.fill(mr, y, r, y + 2, white);

        guiGraphics.drawCenteredString(font, msg, tx, ty, 0xFFFFFF);
    }
}