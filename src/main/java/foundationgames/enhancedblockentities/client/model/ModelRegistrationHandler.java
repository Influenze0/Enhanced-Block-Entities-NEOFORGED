package foundationgames.enhancedblockentities.client.model;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.SpriteGetter;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

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
            ModelResourceLocation modelResLoc = plugin.getId();
            event.register(modelResLoc.id());
        }
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ModelBakery.BakingResult bakingResult = event.getBakingResult();
        Function<Material, TextureAtlasSprite> textureGetter = event.getTextureGetter();
        ModelBakery modelBakery = event.getModelBakery();
        Map<ResourceLocation, BakedModel> standaloneModels = bakingResult.standaloneModels();

        for (DynamicModelProvidingPlugin plugin : PLUGINS) {
            try {
                var unbakedModel = plugin.getModel();
                ModelState modelState = BlockModelRotation.X0_Y0;

                SpriteGetter spriteGetter = createSpriteGetter(textureGetter, plugin.getId());

                ModelBaker baker = createModelBaker(modelBakery, standaloneModels, spriteGetter, plugin.getId());

                var bakedModel = unbakedModel.bake(
                        TextureSlots.EMPTY,
                        baker,
                        modelState,
                        true,
                        true,
                        ItemTransforms.NO_TRANSFORMS
                );

                if (bakedModel != null) {
                    standaloneModels.put(plugin.getId().id(), bakedModel);
                }
            } catch (Exception e) {

            }
        }
    }

    private static SpriteGetter createSpriteGetter(Function<Material, TextureAtlasSprite> textureGetter,
                                                   ModelResourceLocation modelId) {
        return new SpriteGetter() {
            @Override
            public TextureAtlasSprite get(Material material) {
                return textureGetter.apply(material);
            }

            @Override
            public TextureAtlasSprite reportMissingReference(String reference) {
                return textureGetter.apply(new Material(
                        net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS,
                        net.minecraft.client.renderer.texture.MissingTextureAtlasSprite.getLocation()
                ));
            }
        };
    }

    private static ModelBaker createModelBaker(ModelBakery modelBakery,
                                               Map<ResourceLocation, BakedModel> standaloneModels,
                                               SpriteGetter spriteGetter,
                                               ModelResourceLocation modelId) {
        return new ModelBaker() {
            @Override
            public UnbakedModel getModel(ResourceLocation location) {

                UnbakedModel model = modelBakery.unbakedPlainModels.get(location);
                if (model != null) {
                    return model;
                }

                return modelBakery.missingModel;
            }

            @Override
            public BakedModel bake(ResourceLocation location, ModelState state) {

                BakedModel model = standaloneModels.get(location);
                if (model != null) {
                    return model;
                }


                UnbakedModel unbaked = getModel(location);
                if (unbaked != null && unbaked != modelBakery.missingModel) {
                    try {
                        return unbaked.bake(
                                TextureSlots.EMPTY,
                                this,
                                state,
                                true,
                                true,
                                ItemTransforms.NO_TRANSFORMS
                        );
                    } catch (Exception e) {
                    }
                }

                return null;
            }

            @Override
            public SpriteGetter sprites() {
                return spriteGetter;
            }

            @Override
            public ModelDebugName rootName() {
                return () -> modelId.toString();
            }
        };
    }
}