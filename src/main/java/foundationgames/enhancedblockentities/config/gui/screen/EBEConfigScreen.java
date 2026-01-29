package foundationgames.enhancedblockentities.config.gui.screen;

import com.google.common.collect.ImmutableList;
import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.ReloadType;
import foundationgames.enhancedblockentities.config.EBEConfig;
import foundationgames.enhancedblockentities.config.gui.option.EBEOption;
import foundationgames.enhancedblockentities.config.gui.option.TextPalette;
import foundationgames.enhancedblockentities.config.gui.widget.SectionTextWidget;
import foundationgames.enhancedblockentities.config.gui.widget.WidgetRowListWidget;
import foundationgames.enhancedblockentities.util.EBEUtil;
import foundationgames.enhancedblockentities.util.GuiUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class EBEConfigScreen extends Screen {
    private WidgetRowListWidget optionsWidget;
    private final List<EBEOption> options = new ArrayList<>();
    private final Screen parent;

    private static final ImmutableList<String> BOOLEAN_OPTIONS = ImmutableList.of("true", "false");
    private static final ImmutableList<String> ALLOWED_FORCED_DISABLED = ImmutableList.of("allowed", "forced", "disabled");
    private static final ImmutableList<String> SIGN_TEXT_OPTIONS = ImmutableList.of("smart", "all", "most", "some", "few");

    private static final Component HOLD_SHIFT = Component.translatable("text.ebe.descriptions").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    private static final Component CHEST_OPTIONS_TITLE = Component.translatable("text.ebe.chest_options");
    private static final Component SIGN_OPTIONS_TITLE = Component.translatable("text.ebe.sign_options");
    private static final Component BED_OPTIONS_TITLE = Component.translatable("text.ebe.bed_options");
    private static final Component SHULKER_BOX_OPTIONS_TITLE = Component.translatable("text.ebe.shulker_box_options");
    private static final Component ADVANCED_TITLE = Component.translatable("text.ebe.advanced");

    private static final Component DUMP_LABEL = Component.translatable("option.ebe.dump");

    private final Component dumpTooltip = GuiUtil.shorten(I18n.get("option.ebe.dump.comment"), 20);

    public EBEConfigScreen(Screen screen) {
        super(Component.translatable("screen.ebe.config"));
        parent = screen;
    }

    @Override
    protected void init() {
        super.init();

        this.optionsWidget = new WidgetRowListWidget(this.minecraft, this.width, this.height - 69, 34, 316, 20);
        this.options.clear();

        addOptions();
        this.addRenderableWidget(optionsWidget);

        var menuButtons = new GridLayout();
        menuButtons.columnSpacing(4);

        var menuButtonAdder = menuButtons.createRowHelper(3);

        menuButtonAdder.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose())
                .size(100, 20).build());
        menuButtonAdder.addChild(Button.builder(Component.translatable("text.ebe.apply"), button -> this.applyChanges())
                .size(100, 20).build());
        menuButtonAdder.addChild(Button.builder(CommonComponents.GUI_DONE,
                        button -> {
                            applyChanges();
                            onClose();
                        })
                .size(100, 20).build());

        menuButtons.arrangeElements();
        menuButtons.setPosition((this.width - menuButtons.getWidth()) / 2, this.height - 27);
        menuButtons.visitWidgets((child) -> {
            child.setTabOrderGroup(1);
            this.addRenderableWidget(child);
        });
    }

    @Override
    protected void renderMenuBackground(GuiGraphics guiGraphics) {
        renderMenuBackground(guiGraphics, 0, 0, this.width, 34);
        renderMenuBackground(guiGraphics, 0, this.height - 35, this.width, 35);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);

        guiGraphics.drawCenteredString(this.font, this.title, (int)(this.width * 0.5), 8, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, HOLD_SHIFT, (int)(this.width * 0.5), 21, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    public void applyChanges() {
        EBEConfig config = EnhancedBlockEntities.CONFIG;
        Properties properties = new Properties();
        AtomicReference<ReloadType> type = new AtomicReference<>(ReloadType.NONE);
        options.forEach(option -> {
            if (!option.isDefault()) {
                type.set(type.get().or(option.reloadType));
            }
            properties.setProperty(option.key, option.getValue());
        });
        config.readFrom(properties);
        config.save();
        EnhancedBlockEntities.reload(type.get());
    }

    public void addOptions() {
        Properties config = new Properties();
        EnhancedBlockEntities.CONFIG.writeTo(config);

        final var font = this.minecraft.font;

        optionsWidget.add(new SectionTextWidget(CHEST_OPTIONS_TITLE, font));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.RENDER_ENHANCED_CHESTS_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.RENDER_ENHANCED_CHESTS_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.CHEST_AO_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.CHEST_AO_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.EXPERIMENTAL_CHESTS_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.EXPERIMENTAL_CHESTS_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ), option(
                new EBEOption(EBEConfig.CHRISTMAS_CHESTS_KEY, ALLOWED_FORCED_DISABLED, ALLOWED_FORCED_DISABLED.indexOf(config.getProperty(EBEConfig.CHRISTMAS_CHESTS_KEY)), true, TextPalette.rainbow(0.35f), ReloadType.WORLD)
        ));

        optionsWidget.add(new SectionTextWidget(SIGN_OPTIONS_TITLE, font));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.RENDER_ENHANCED_SIGNS_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.RENDER_ENHANCED_SIGNS_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.SIGN_TEXT_RENDERING_KEY, SIGN_TEXT_OPTIONS, SIGN_TEXT_OPTIONS.indexOf(config.getProperty(EBEConfig.SIGN_TEXT_RENDERING_KEY)), true, TextPalette.rainbow(0.45f), ReloadType.NONE)
        ));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.EXPERIMENTAL_SIGNS_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.EXPERIMENTAL_SIGNS_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ), option(
                new EBEOption(EBEConfig.SIGN_AO_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.SIGN_AO_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));

        optionsWidget.add(new SectionTextWidget(BED_OPTIONS_TITLE, font));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.RENDER_ENHANCED_BEDS_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.RENDER_ENHANCED_BEDS_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.EXPERIMENTAL_BEDS_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.EXPERIMENTAL_BEDS_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ), option(
                new EBEOption(EBEConfig.BED_AO_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.BED_AO_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));

        optionsWidget.add(new SectionTextWidget(SHULKER_BOX_OPTIONS_TITLE, font));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.RENDER_ENHANCED_SHULKER_BOXES_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.RENDER_ENHANCED_SHULKER_BOXES_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.SHULKER_BOX_AO_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.SHULKER_BOX_AO_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));

        optionsWidget.add(new SectionTextWidget(ADVANCED_TITLE, font));
        optionsWidget.add(option(
                new EBEOption(EBEConfig.FORCE_RESOURCE_PACK_COMPAT_KEY, BOOLEAN_OPTIONS, BOOLEAN_OPTIONS.indexOf(config.getProperty(EBEConfig.FORCE_RESOURCE_PACK_COMPAT_KEY)), false, TextPalette.ON_OFF, ReloadType.RESOURCES)
        ));
        optionsWidget.add(Button.builder(DUMP_LABEL, b -> {
            try {
                EBEUtil.dumpResources();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).tooltip(Tooltip.create(dumpTooltip)).build());
    }

    private Button option(EBEOption option) {
        options.add(option);

        return Button.builder(option.getText(), b -> {
            option.next();
            b.setMessage(option.getText());
            b.setTooltip(option.getTooltip());
        }).tooltip(option.getTooltip()).build();
    }
}