package foundationgames.enhancedblockentities;

import foundationgames.enhancedblockentities.client.model.DynamicModelEffects;
import foundationgames.enhancedblockentities.client.model.DynamicModelProvidingPlugin;
import foundationgames.enhancedblockentities.client.model.DynamicUnbakedModel;
import foundationgames.enhancedblockentities.client.model.ModelIdentifiers;
import foundationgames.enhancedblockentities.client.model.ModelRegistrationHandler;
import foundationgames.enhancedblockentities.client.model.ModelSelector;
import foundationgames.enhancedblockentities.client.render.BlockEntityRenderCondition;
import foundationgames.enhancedblockentities.client.render.BlockEntityRendererOverride;
import foundationgames.enhancedblockentities.client.render.entity.ChestBlockEntityRendererOverride;
import foundationgames.enhancedblockentities.client.render.entity.ShulkerBoxBlockEntityRendererOverride;
import foundationgames.enhancedblockentities.client.render.entity.SignBlockEntityRendererOverride;
import foundationgames.enhancedblockentities.client.resource.EBEPack;
import foundationgames.enhancedblockentities.util.DateUtil;
import foundationgames.enhancedblockentities.util.EBEUtil;
import foundationgames.enhancedblockentities.util.ResourceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.function.Function;

public enum EBESetup {
    ;

    public static void setupRRPChests() {
        EBEPack p = ResourceUtil.getPackForCompat();

        ResourceUtil.addChestBlockStates("chest", p);
        ResourceUtil.addChestBlockStates("trapped_chest", p);
        ResourceUtil.addChestBlockStates("christmas_chest", p);
        ResourceUtil.addSingleChestOnlyBlockStates("ender_chest", p);

        p = ResourceUtil.getBasePack();

        ResourceUtil.addSingleChestModels("normal", "chest", p);
        ResourceUtil.addDoubleChestModels("normal_left", "normal_right", "chest", p);
        ResourceUtil.addSingleChestModels("trapped", "trapped_chest", p);
        ResourceUtil.addDoubleChestModels("trapped_left", "trapped_right", "trapped_chest", p);
        ResourceUtil.addSingleChestModels("christmas", "christmas_chest", p);
        ResourceUtil.addDoubleChestModels("christmas_left", "christmas_right", "christmas_chest", p);
        ResourceUtil.addSingleChestModels("ender", "ender_chest", p);

        ResourceUtil.addChestItemModel(ResourceLocation.withDefaultNamespace("models/item/chest.json"), "chest_center", p);
        ResourceUtil.addChestItemModel(ResourceLocation.withDefaultNamespace("models/item/trapped_chest.json"), "trapped_chest_center", p);
        ResourceUtil.addParentModel(EnhancedBlockEntities.MOD_ID + ":block/ender_chest_center",
                ResourceLocation.withDefaultNamespace("item/ender_chest"), p);

        p.addDirBlockSprites("entity/chest", "entity/chest/");
    }

    public static void setupRRPSigns() {
        EBEPack p = ResourceUtil.getPackForCompat();

        ResourceUtil.addSignBlockStates("oak_sign", "oak_wall_sign", p);
        ResourceUtil.addSignBlockStates("birch_sign", "birch_wall_sign", p);
        ResourceUtil.addSignBlockStates("spruce_sign", "spruce_wall_sign", p);
        ResourceUtil.addSignBlockStates("jungle_sign", "jungle_wall_sign", p);
        ResourceUtil.addSignBlockStates("acacia_sign", "acacia_wall_sign", p);
        ResourceUtil.addSignBlockStates("dark_oak_sign", "dark_oak_wall_sign", p);
        ResourceUtil.addSignBlockStates("mangrove_sign", "mangrove_wall_sign", p);
        ResourceUtil.addSignBlockStates("cherry_sign", "cherry_wall_sign", p);
        ResourceUtil.addSignBlockStates("crimson_sign", "crimson_wall_sign", p);
        ResourceUtil.addSignBlockStates("warped_sign", "warped_wall_sign", p);
        ResourceUtil.addSignBlockStates("bamboo_sign", "bamboo_wall_sign", p);

        ResourceUtil.addHangingSignBlockStates("oak_hanging_sign", "oak_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("birch_hanging_sign", "birch_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("spruce_hanging_sign", "spruce_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("jungle_hanging_sign", "jungle_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("acacia_hanging_sign", "acacia_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("dark_oak_hanging_sign", "dark_oak_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("mangrove_hanging_sign", "mangrove_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("cherry_hanging_sign", "cherry_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("crimson_hanging_sign", "crimson_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("warped_hanging_sign", "warped_wall_hanging_sign", p);
        ResourceUtil.addHangingSignBlockStates("bamboo_hanging_sign", "bamboo_wall_hanging_sign", p);

        p = ResourceUtil.getBasePack();

        ResourceUtil.addSignTypeModels("oak", p);
        ResourceUtil.addSignTypeModels("birch", p);
        ResourceUtil.addSignTypeModels("spruce", p);
        ResourceUtil.addSignTypeModels("jungle", p);
        ResourceUtil.addSignTypeModels("acacia", p);
        ResourceUtil.addSignTypeModels("dark_oak", p);
        ResourceUtil.addSignTypeModels("mangrove", p);
        ResourceUtil.addSignTypeModels("cherry", p);
        ResourceUtil.addSignTypeModels("crimson", p);
        ResourceUtil.addSignTypeModels("warped", p);
        ResourceUtil.addSignTypeModels("bamboo", p);

        p.addDirBlockSprites("entity/signs", "entity/signs/");
        p.addDirBlockSprites("entity/signs/hanging", "entity/signs/hanging/");
        p.addDirBlockSprites("gui/hanging_signs", "block/particle_hanging_sign_");
    }


    public static void setupRRPBeds() {
        EBEPack p = ResourceUtil.getBasePack();
        EBEPack pCompat = ResourceUtil.getPackForCompat();

        for (DyeColor color : DyeColor.values()) {
            ResourceUtil.addBedBlockState(color, pCompat);
            ResourceUtil.addBedModels(color, p);
        }

        p.addDirBlockSprites("entity/bed", "entity/bed/");
    }

    public static void setupRRPShulkerBoxes() {
        EBEPack p = ResourceUtil.getBasePack();
        EBEPack pCompat = ResourceUtil.getPackForCompat();

        for (DyeColor color : EBEUtil.DEFAULTED_DYE_COLORS) {
            var id = color != null ? color.getName() + "_shulker_box" : "shulker_box";
            ResourceUtil.addShulkerBoxBlockStates(color, pCompat);
            ResourceUtil.addShulkerBoxModels(color, p);
            ResourceUtil.addParentModel(EnhancedBlockEntities.MOD_ID + ":block/" + id,
                    ResourceLocation.withDefaultNamespace("item/" + id), p);
        }

        p.addDirBlockSprites("entity/shulker", "entity/shulker/");
    }

    public static void setupResourceProviders() {
        ModelRegistrationHandler.register(new DynamicModelProvidingPlugin(
                EBEUtil.id("block/chest_center"),
                () -> new DynamicUnbakedModel(
                        new ResourceLocation[]{
                                ModelIdentifiers.CHEST_CENTER,
                                ModelIdentifiers.CHEST_CENTER_TRUNK,
                                ModelIdentifiers.CHRISTMAS_CHEST_CENTER,
                                ModelIdentifiers.CHRISTMAS_CHEST_CENTER_TRUNK
                        },
                        ModelSelector.CHEST_WITH_CHRISTMAS,
                        DynamicModelEffects.CHEST
                )
        ));
        ModelRegistrationHandler.register(new DynamicModelProvidingPlugin(
                EBEUtil.id("block/chest_left"),
                () -> new DynamicUnbakedModel(
                        new ResourceLocation[]{
                                ModelIdentifiers.CHEST_LEFT,
                                ModelIdentifiers.CHEST_LEFT_TRUNK,
                                ModelIdentifiers.CHRISTMAS_CHEST_LEFT,
                                ModelIdentifiers.CHRISTMAS_CHEST_LEFT_TRUNK
                        },
                        ModelSelector.CHEST_WITH_CHRISTMAS,
                        DynamicModelEffects.CHEST
                )
        ));
        ModelRegistrationHandler.register(new DynamicModelProvidingPlugin(
                EBEUtil.id("block/chest_right"),
                () -> new DynamicUnbakedModel(
                        new ResourceLocation[]{
                                ModelIdentifiers.CHEST_RIGHT,
                                ModelIdentifiers.CHEST_RIGHT_TRUNK,
                                ModelIdentifiers.CHRISTMAS_CHEST_RIGHT,
                                ModelIdentifiers.CHRISTMAS_CHEST_RIGHT_TRUNK
                        },
                        ModelSelector.CHEST_WITH_CHRISTMAS,
                        DynamicModelEffects.CHEST
                )
        ));
        ModelRegistrationHandler.register(new DynamicModelProvidingPlugin(
                EBEUtil.id("block/trapped_chest_center"),
                () -> new DynamicUnbakedModel(
                        new ResourceLocation[]{
                                ModelIdentifiers.TRAPPED_CHEST_CENTER,
                                ModelIdentifiers.TRAPPED_CHEST_CENTER_TRUNK,
                                ModelIdentifiers.CHRISTMAS_CHEST_CENTER,
                                ModelIdentifiers.CHRISTMAS_CHEST_CENTER_TRUNK
                        },
                        ModelSelector.CHEST_WITH_CHRISTMAS,
                        DynamicModelEffects.CHEST
                )
        ));
        ModelRegistrationHandler.register(new DynamicModelProvidingPlugin(
                EBEUtil.id("block/trapped_chest_left"),
                () -> new DynamicUnbakedModel(
                        new ResourceLocation[]{
                                ModelIdentifiers.TRAPPED_CHEST_LEFT,
                                ModelIdentifiers.TRAPPED_CHEST_LEFT_TRUNK,
                                ModelIdentifiers.CHRISTMAS_CHEST_LEFT,
                                ModelIdentifiers.CHRISTMAS_CHEST_LEFT_TRUNK
                        },
                        ModelSelector.CHEST_WITH_CHRISTMAS,
                        DynamicModelEffects.CHEST
                )
        ));
        ModelRegistrationHandler.register(new DynamicModelProvidingPlugin(
                EBEUtil.id("block/trapped_chest_right"),
                () -> new DynamicUnbakedModel(
                        new ResourceLocation[]{
                                ModelIdentifiers.TRAPPED_CHEST_RIGHT,
                                ModelIdentifiers.TRAPPED_CHEST_RIGHT_TRUNK,
                                ModelIdentifiers.CHRISTMAS_CHEST_RIGHT,
                                ModelIdentifiers.CHRISTMAS_CHEST_RIGHT_TRUNK
                        },
                        ModelSelector.CHEST_WITH_CHRISTMAS,
                        DynamicModelEffects.CHEST
                )
        ));
        ModelRegistrationHandler.register(new DynamicModelProvidingPlugin(
                EBEUtil.id("block/ender_chest_center"),
                () -> new DynamicUnbakedModel(
                        new ResourceLocation[]{
                                ModelIdentifiers.ENDER_CHEST_CENTER,
                                ModelIdentifiers.ENDER_CHEST_CENTER_TRUNK
                        },
                        ModelSelector.CHEST,
                        DynamicModelEffects.CHEST
                )
        ));
        for (DyeColor color : EBEUtil.DEFAULTED_DYE_COLORS) {
            String boxName = color != null ? color.getName() + "_shulker_box" : "shulker_box";
            final DyeColor finalColor = color;
            ModelRegistrationHandler.register(new DynamicModelProvidingPlugin(
                    EBEUtil.id("block/" + boxName),
                    () -> new DynamicUnbakedModel(
                            new ResourceLocation[]{
                                    ModelIdentifiers.SHULKER_BOXES.get(finalColor),
                                    ModelIdentifiers.SHULKER_BOX_BOTTOMS.get(finalColor)
                            },
                            ModelSelector.SHULKER_BOX,
                            DynamicModelEffects.SHULKER_BOX
                    )
            ));
        }
    }

    public static void setupChests() {
        Function<BlockEntity, Integer> christmasChestSelector = entity -> {
            int os = DateUtil.isChristmas() ? 3 : 0;
            ChestType type = entity.getBlockState().getValue(BlockStateProperties.CHEST_TYPE);
            return type == ChestType.RIGHT ? 2 + os : type == ChestType.LEFT ? 1 + os : os;
        };

        EnhancedBlockEntityRegistry.register(Blocks.CHEST, BlockEntityType.CHEST, BlockEntityRenderCondition.CHEST,
                new ChestBlockEntityRendererOverride(() -> {
                    var manager = Minecraft.getInstance().getModelManager();
                    return new BakedModel[]{
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHEST_CENTER_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHEST_LEFT_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHEST_RIGHT_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHRISTMAS_CHEST_CENTER_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHRISTMAS_CHEST_LEFT_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHRISTMAS_CHEST_RIGHT_LID))
                    };
                }, christmasChestSelector)
        );
        EnhancedBlockEntityRegistry.register(Blocks.TRAPPED_CHEST, BlockEntityType.TRAPPED_CHEST, BlockEntityRenderCondition.CHEST,
                new ChestBlockEntityRendererOverride(() -> {
                    var manager = Minecraft.getInstance().getModelManager();
                    return new BakedModel[]{
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.TRAPPED_CHEST_CENTER_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.TRAPPED_CHEST_LEFT_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.TRAPPED_CHEST_RIGHT_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHRISTMAS_CHEST_CENTER_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHRISTMAS_CHEST_LEFT_LID)),
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.CHRISTMAS_CHEST_RIGHT_LID))
                    };
                }, christmasChestSelector)
        );
        EnhancedBlockEntityRegistry.register(Blocks.ENDER_CHEST, BlockEntityType.ENDER_CHEST, BlockEntityRenderCondition.CHEST,
                new ChestBlockEntityRendererOverride(() -> {
                    var manager = Minecraft.getInstance().getModelManager();
                    return new BakedModel[]{
                            manager.getModel(ModelIdentifiers.toModelResourceLocation(ModelIdentifiers.ENDER_CHEST_CENTER_LID))
                    };
                }, entity -> 0)
        );
    }

    public static void setupSigns() {
        for (var sign : new Block[]{
                Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN,
                Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN,
                Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN,
                Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN,
                Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN,
                Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN,
                Blocks.MANGROVE_SIGN, Blocks.MANGROVE_WALL_SIGN,
                Blocks.CHERRY_SIGN, Blocks.CHERRY_WALL_SIGN,
                Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN,
                Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN,
                Blocks.BAMBOO_SIGN, Blocks.BAMBOO_WALL_SIGN
        }) {
            EnhancedBlockEntityRegistry.register(sign, BlockEntityType.SIGN, BlockEntityRenderCondition.SIGN,
                    new SignBlockEntityRendererOverride()
            );
        }

        for (var sign : new Block[]{
                Blocks.OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN,
                Blocks.BIRCH_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN,
                Blocks.SPRUCE_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN,
                Blocks.JUNGLE_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN,
                Blocks.ACACIA_HANGING_SIGN, Blocks.ACACIA_WALL_HANGING_SIGN,
                Blocks.DARK_OAK_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN,
                Blocks.MANGROVE_HANGING_SIGN, Blocks.MANGROVE_WALL_HANGING_SIGN,
                Blocks.CHERRY_HANGING_SIGN, Blocks.CHERRY_WALL_HANGING_SIGN,
                Blocks.CRIMSON_HANGING_SIGN, Blocks.CRIMSON_WALL_HANGING_SIGN,
                Blocks.WARPED_HANGING_SIGN, Blocks.WARPED_WALL_HANGING_SIGN,
                Blocks.BAMBOO_HANGING_SIGN, Blocks.BAMBOO_WALL_HANGING_SIGN
        }) {
            EnhancedBlockEntityRegistry.register(sign, BlockEntityType.HANGING_SIGN, BlockEntityRenderCondition.SIGN,
                    new SignBlockEntityRendererOverride()
            );
        }
    }


    public static void setupBeds() {
        EnhancedBlockEntityRegistry.register(Blocks.BLACK_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.BLUE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.BROWN_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.CYAN_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.GRAY_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.GREEN_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIGHT_BLUE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIGHT_GRAY_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.LIME_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.MAGENTA_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.ORANGE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.PINK_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.PURPLE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.RED_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.WHITE_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
        EnhancedBlockEntityRegistry.register(Blocks.YELLOW_BED, BlockEntityType.BED, BlockEntityRenderCondition.NEVER, BlockEntityRendererOverride.NO_OP);
    }

    public static void setupShulkerBoxes() {
        for (DyeColor color : EBEUtil.DEFAULTED_DYE_COLORS) {
            var block = ShulkerBoxBlock.getBlockByColor(color);
            EnhancedBlockEntityRegistry.register(block, BlockEntityType.SHULKER_BOX, BlockEntityRenderCondition.SHULKER_BOX,
                    new ShulkerBoxBlockEntityRendererOverride((map) -> {
                        var models = Minecraft.getInstance().getModelManager();
                        for (DyeColor dc : EBEUtil.DEFAULTED_DYE_COLORS) {
                            map.put(dc, models.getModel(ModelIdentifiers.getShulkerBoxLidModel(dc)));
                        }
                    })
            );
        }
    }
}