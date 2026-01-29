package foundationgames.enhancedblockentities.client.model;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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
    public void resolveDependencies(Resolver resolver) {
        for (ResourceLocation modelId : models) {
            resolver.resolve(modelId);
        }
    }

    @Override
    public @Nullable BakedModel bake(
            TextureSlots textureSlots,
            ModelBaker baker,
            ModelState state,
            boolean useAmbientOcclusion,
            boolean usesBlockLight,
            ItemTransforms transforms) {

        BakedModel[] baked = new BakedModel[models.length];

        for (int i = 0; i < models.length; i++) {

            UnbakedModel unbaked = baker.getModel(models[i]);
            if (unbaked != null) {
                baked[i] = unbaked.bake(
                        textureSlots,
                        baker,
                        state,
                        useAmbientOcclusion,
                        usesBlockLight,
                        transforms
                );
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