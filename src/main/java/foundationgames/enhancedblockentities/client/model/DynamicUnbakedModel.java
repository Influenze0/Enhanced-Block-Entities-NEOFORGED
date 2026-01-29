package foundationgames.enhancedblockentities.client.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class DynamicUnbakedModel implements UnbakedModel {
    private final ResourceLocation[] models;
    private final ModelSelector selector;
    private final DynamicModelEffects effects;

    public DynamicUnbakedModel(ResourceLocation[] models, ModelSelector selector, DynamicModelEffects effects) {
        this.models = models;
        this.selector = selector;
        this.effects = effects;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return List.of(models);
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
        for (ResourceLocation modelId : models) {
            UnbakedModel model = resolver.apply(modelId);
            if (model != null) {
                model.resolveParents(resolver);
            }
        }
    }

    @Override
    public @Nullable BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter,
                                     ModelState state) {
        BakedModel[] baked = new BakedModel[models.length];
        for (int i = 0; i < models.length; i++) {
            UnbakedModel unbaked = baker.getModel(models[i]);
            if (unbaked != null) {
                baked[i] = unbaked.bake(baker, spriteGetter, state);
            } else {
                baked[i] = baker.bake(models[i], state);
            }
        }
        return new DynamicBakedModel(baked, selector, effects);
    }

    public ResourceLocation[] getModelIds() {
        return models;
    }

    public ModelSelector getSelector() {
        return selector;
    }

    public DynamicModelEffects getEffects() {
        return effects;
    }
}