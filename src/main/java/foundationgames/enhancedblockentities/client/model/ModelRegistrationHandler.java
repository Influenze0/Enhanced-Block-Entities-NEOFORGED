package foundationgames.enhancedblockentities.client.model;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@EventBusSubscriber(modid = EnhancedBlockEntities.MOD_ID, value = Dist.CLIENT)
public class ModelRegistrationHandler {
    private static final List<DynamicModelProvidingPlugin> PLUGINS = new ArrayList<>();

    public static void register(DynamicModelProvidingPlugin plugin) {
        PLUGINS.add(plugin);
    }

    public static void clearPlugins() {
        PLUGINS.clear();
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        for (DynamicModelProvidingPlugin plugin : PLUGINS) {
            event.register(plugin.getId());
        }
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> modelRegistry = event.getModels();

        for (DynamicModelProvidingPlugin plugin : PLUGINS) {
            try {
                var unbakedModel = plugin.getModel();

                ModelState modelState = BlockModelRotation.X0_Y0;

                Function<Material, TextureAtlasSprite> spriteGetter = material -> {
                    var atlasTexture = Minecraft.getInstance().getModelManager().getAtlas(material.atlasLocation());
                    return atlasTexture.getSprite(material.texture());
                };

                ModelBaker baker = createModelBaker(modelRegistry, spriteGetter);

                var bakedModel = unbakedModel.bake(
                        baker,
                        spriteGetter,
                        modelState
                );

                if (bakedModel != null) {
                    modelRegistry.put(plugin.getId(), bakedModel);
                }
            } catch (Exception e) {
            }
        }
    }

    private static ModelBaker createModelBaker(Map<ModelResourceLocation, BakedModel> modelRegistry,
                                               Function<Material, TextureAtlasSprite> spriteGetter) {
        return new ModelBaker() {
            @Override
            public UnbakedModel getModel(ResourceLocation location) {
                return null;
            }

            @Override
            public BakedModel bake(ResourceLocation location, ModelState state) {
                for (var entry : modelRegistry.entrySet()) {
                    if (entry.getKey().id().equals(location)) {
                        return entry.getValue();
                    }
                }
                var standaloneKey = new ModelResourceLocation(location, "standalone");
                return modelRegistry.get(standaloneKey);
            }

            @Override
            public @Nullable UnbakedModel getTopLevelModel(ModelResourceLocation modelResourceLocation) {
                return null;
            }

            @Override
            public @Nullable BakedModel bake(ResourceLocation resourceLocation, ModelState modelState, Function<Material, TextureAtlasSprite> function) {
                return bake(resourceLocation, modelState);
            }

            @Override
            public @Nullable BakedModel bakeUncached(UnbakedModel unbakedModel, ModelState modelState, Function<Material, TextureAtlasSprite> function) {
                return null;
            }

            @Override
            public Function<Material, TextureAtlasSprite> getModelTextureGetter() {
                return spriteGetter;
            }
        };
    }
}