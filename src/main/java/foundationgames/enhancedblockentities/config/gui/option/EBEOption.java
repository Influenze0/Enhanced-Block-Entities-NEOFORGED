package foundationgames.enhancedblockentities.config.gui.option;

import foundationgames.enhancedblockentities.ReloadType;
import foundationgames.enhancedblockentities.util.GuiUtil;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import java.util.List;

public final class EBEOption {
    private static final Component NEWLINE = Component.literal("\n");
    private static final String OPTION_VALUE = "options.generic_value";
    private static final String DIVIDER = "text.ebe.option_value_division";

    public final String key;
    public final boolean hasValueComments;
    public final Component comment;
    public final ReloadType reloadType;
    public final TextPalette palette;

    private final List<String> values;
    private final int defaultValue;

    private int selected;
    private Tooltip tooltip = null;
    private Component text = null;

    public EBEOption(String key, List<String> values, int defaultValue, boolean hasValueComments, TextPalette palette, ReloadType reloadType) {
        this.key = key;
        this.values = values;
        this.defaultValue = Mth.clamp(defaultValue, 0, values.size());
        this.selected = this.defaultValue;
        this.hasValueComments = hasValueComments;
        this.palette = palette;
        this.reloadType = reloadType;

        String commentKey = I18n.get(String.format("option.ebe.%s.comment", key));
        comment = GuiUtil.shorten(commentKey, 20);
    }

    public String getValue() {
        return values.get(selected);
    }

    public String getOptionKey() {
        return String.format("option.ebe.%s", key);
    }

    public String getValueKey() {
        return String.format("value.ebe.%s", getValue());
    }

    public Component getText() {
        MutableComponent option = Component.translatable(this.getOptionKey()).withStyle(style -> style.withColor(isDefault() ? 0xFFFFFF : 0xFFDA5E));
        MutableComponent value = Component.translatable(this.getValueKey()).withStyle(style -> style.withColor(this.palette.getColor((float) this.selected / this.values.size())));

        if (text == null) {
            text = option.append(Component.translatable(DIVIDER).append(value));
        }
        return text;
    }

    public Tooltip getTooltip() {
        if (tooltip == null) {
            if (hasValueComments) {
                tooltip = Tooltip.create(
                        Component.translatable(String.format("option.ebe.%s.valueComment.%s", key, getValue()))
                                .append(NEWLINE)
                                .append(comment.copy())
                );
            } else {
                tooltip = Tooltip.create(comment.copy());
            }
        }
        return tooltip;
    }

    public void next() {
        selected++;
        if (selected >= values.size()) {
            selected = 0;
        }
        tooltip = null;
        text = null;
    }

    public boolean isDefault() {
        return selected == defaultValue;
    }
}