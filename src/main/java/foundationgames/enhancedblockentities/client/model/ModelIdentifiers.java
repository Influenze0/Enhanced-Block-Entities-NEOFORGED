package foundationgames.enhancedblockentities.client.model;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.config.EBEConfig;
import foundationgames.enhancedblockentities.util.EBEUtil;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@EventBusSubscriber(modid = EnhancedBlockEntities.MOD_ID)
public final class ModelIdentifiers {
    private static final Map<Predicate<EBEConfig>, Set<ResourceLocation>> modelLoaders = new HashMap<>();
    private static final Map<ResourceLocation, ModelResourceLocation> modelResourceLocationCache = new HashMap<>();

    public static final Predicate<EBEConfig> CHEST_PREDICATE = c -> c.renderEnhancedChests;
    public static final Predicate<EBEConfig> SHULKER_BOX_PREDICATE = c -> c.renderEnhancedShulkerBoxes;

    public static final ResourceLocation CHEST_CENTER = of("block/chest_center", CHEST_PREDICATE);
    public static final ResourceLocation CHEST_CENTER_TRUNK = of("block/chest_center_trunk", CHEST_PREDICATE);
    public static final ResourceLocation CHEST_CENTER_LID = of("block/chest_center_lid", CHEST_PREDICATE);
    public static final ResourceLocation CHEST_LEFT = of("block/chest_left", CHEST_PREDICATE);
    public static final ResourceLocation CHEST_LEFT_TRUNK = of("block/chest_left_trunk", CHEST_PREDICATE);
    public static final ResourceLocation CHEST_LEFT_LID = of("block/chest_left_lid", CHEST_PREDICATE);
    public static final ResourceLocation CHEST_RIGHT = of("block/chest_right", CHEST_PREDICATE);
    public static final ResourceLocation CHEST_RIGHT_TRUNK = of("block/chest_right_trunk", CHEST_PREDICATE);
    public static final ResourceLocation CHEST_RIGHT_LID = of("block/chest_right_lid", CHEST_PREDICATE);

    public static final ResourceLocation TRAPPED_CHEST_CENTER = of("block/trapped_chest_center", CHEST_PREDICATE);
    public static final ResourceLocation TRAPPED_CHEST_CENTER_TRUNK = of("block/trapped_chest_center_trunk", CHEST_PREDICATE);
    public static final ResourceLocation TRAPPED_CHEST_CENTER_LID = of("block/trapped_chest_center_lid", CHEST_PREDICATE);
    public static final ResourceLocation TRAPPED_CHEST_LEFT = of("block/trapped_chest_left", CHEST_PREDICATE);
    public static final ResourceLocation TRAPPED_CHEST_LEFT_TRUNK = of("block/trapped_chest_left_trunk", CHEST_PREDICATE);
    public static final ResourceLocation TRAPPED_CHEST_LEFT_LID = of("block/trapped_chest_left_lid", CHEST_PREDICATE);
    public static final ResourceLocation TRAPPED_CHEST_RIGHT = of("block/trapped_chest_right", CHEST_PREDICATE);
    public static final ResourceLocation TRAPPED_CHEST_RIGHT_TRUNK = of("block/trapped_chest_right_trunk", CHEST_PREDICATE);
    public static final ResourceLocation TRAPPED_CHEST_RIGHT_LID = of("block/trapped_chest_right_lid", CHEST_PREDICATE);

    public static final ResourceLocation CHRISTMAS_CHEST_CENTER = of("block/christmas_chest_center", CHEST_PREDICATE);
    public static final ResourceLocation CHRISTMAS_CHEST_CENTER_TRUNK = of("block/christmas_chest_center_trunk", CHEST_PREDICATE);
    public static final ResourceLocation CHRISTMAS_CHEST_CENTER_LID = of("block/christmas_chest_center_lid", CHEST_PREDICATE);
    public static final ResourceLocation CHRISTMAS_CHEST_LEFT = of("block/christmas_chest_left", CHEST_PREDICATE);
    public static final ResourceLocation CHRISTMAS_CHEST_LEFT_TRUNK = of("block/christmas_chest_left_trunk", CHEST_PREDICATE);
    public static final ResourceLocation CHRISTMAS_CHEST_LEFT_LID = of("block/christmas_chest_left_lid", CHEST_PREDICATE);
    public static final ResourceLocation CHRISTMAS_CHEST_RIGHT = of("block/christmas_chest_right", CHEST_PREDICATE);
    public static final ResourceLocation CHRISTMAS_CHEST_RIGHT_TRUNK = of("block/christmas_chest_right_trunk", CHEST_PREDICATE);
    public static final ResourceLocation CHRISTMAS_CHEST_RIGHT_LID = of("block/christmas_chest_right_lid", CHEST_PREDICATE);

    public static final ResourceLocation ENDER_CHEST_CENTER = of("block/ender_chest_center", CHEST_PREDICATE);
    public static final ResourceLocation ENDER_CHEST_CENTER_TRUNK = of("block/ender_chest_center_trunk", CHEST_PREDICATE);
    public static final ResourceLocation ENDER_CHEST_CENTER_LID = of("block/ender_chest_center_lid", CHEST_PREDICATE);

    public static final Map<DyeColor, ResourceLocation> SHULKER_BOXES = new HashMap<>();
    public static final Map<DyeColor, ResourceLocation> SHULKER_BOX_BOTTOMS = new HashMap<>();
    public static final Map<DyeColor, ResourceLocation> SHULKER_BOX_LIDS = new HashMap<>();

    static {
        for (DyeColor color : EBEUtil.DEFAULTED_DYE_COLORS) {
            var id = color != null ? "block/" + color.getName() + "_shulker_box" : "block/shulker_box";
            SHULKER_BOXES.put(color, of(id, SHULKER_BOX_PREDICATE));
            SHULKER_BOX_BOTTOMS.put(color, of(id + "_bottom", SHULKER_BOX_PREDICATE));
            SHULKER_BOX_LIDS.put(color, of(id + "_lid", SHULKER_BOX_PREDICATE));
        }
    }

    public static void init() {
    }

    private static ResourceLocation of(String id, Predicate<EBEConfig> condition) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(EnhancedBlockEntities.MOD_ID, id);
        modelLoaders.computeIfAbsent(condition, k -> new HashSet<>()).add(location);
        modelResourceLocationCache.put(location, new ModelResourceLocation(location, "standalone"));

        return location;
    }

    public static ModelResourceLocation toModelResourceLocation(ResourceLocation location) {
        return modelResourceLocationCache.computeIfAbsent(location,
                loc -> new ModelResourceLocation(loc, "standalone"));
    }

    public static ModelResourceLocation getShulkerBoxLidModel(DyeColor color) {
        return toModelResourceLocation(SHULKER_BOX_LIDS.get(color));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(EnhancedBlockEntities.MOD_ID, path);
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        var config = EnhancedBlockEntities.CONFIG;

        for (var entry : modelLoaders.entrySet()) {
            if (entry.getKey().test(config)) {
                for (ResourceLocation modelId : entry.getValue()) {
                    event.register(toModelResourceLocation(modelId));
                }
            }
        }
    }

    public static Set<ResourceLocation> getModelsForCondition(Predicate<EBEConfig> condition) {
        return modelLoaders.getOrDefault(condition, Set.of());
    }

    public static boolean isModelRegistered(ResourceLocation modelId) {
        return modelLoaders.values().stream().anyMatch(set -> set.contains(modelId));
    }
}