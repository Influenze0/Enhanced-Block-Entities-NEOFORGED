package foundationgames.enhancedblockentities.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicBakedModel implements IDynamicBakedModel {
    private final BakedModel[] models;
    private final ModelSelector selector;
    private final DynamicModelEffects effects;

    private final ThreadLocal<int[]> activeModelIndices;
    private final ThreadLocal<BakedModel[]> displayedModels;

    public DynamicBakedModel(BakedModel[] models, ModelSelector selector, DynamicModelEffects effects) {
        this.models = models;
        this.selector = selector;
        this.effects = effects;

        this.activeModelIndices = ThreadLocal.withInitial(() -> new int[selector.displayedModelCount()]);
        this.displayedModels = ThreadLocal.withInitial(() -> new BakedModel[selector.displayedModelCount()]);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource rand, @NotNull ModelData extraData,
                                             @Nullable RenderType renderType) {
        int[] indices = this.activeModelIndices.get();
        BakedModel[] displayedModels = this.displayedModels.get();

        int[] dataIndices = extraData.get(ModelSelector.MODEL_INDICES_PROPERTY);
        if (dataIndices != null) {
            System.arraycopy(dataIndices, 0, indices, 0, Math.min(dataIndices.length, indices.length));
        } else {
            selector.writeModelIndicesWithoutContext(state, rand, indices);
        }

        for (int i = 0; i < indices.length; i++) {
            int modelIndex = indices[i];
            displayedModels[i] = (modelIndex >= 0 && modelIndex < models.length) ? models[modelIndex] : null;
        }

        List<BakedQuad> quads = new ArrayList<>();
        for (BakedModel model : displayedModels) {
            if (model != null) {
                quads.addAll(model.getQuads(state, side, rand, ModelData.EMPTY, renderType));
            }
        }

        return quads;
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
                                           @NotNull BlockState state, @NotNull ModelData modelData) {
        int[] indices = new int[selector.displayedModelCount()];
        selector.writeModelIndices(level, state, pos, indices);

        return modelData.derive()
                .with(ModelSelector.MODEL_INDICES_PROPERTY, indices)
                .build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        if (models.length > 0 && models[0] != null) {
            return models[0].getQuads(state, direction, random);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return getEffects().ambientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }
    @Override
    public TextureAtlasSprite getParticleIcon() {
        int particleIndex = getSelector().getParticleModelIndex();
        if (particleIndex >= 0 && particleIndex < models.length && models[particleIndex] != null) {
            return models[particleIndex].getParticleIcon();
        }
        return models[0].getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        if (models.length > 0 && models[0] != null) {
            return models[0].getTransforms();
        }
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand,
                                             @NotNull ModelData data) {
        if (models.length > 0 && models[0] != null) {
            return models[0].getRenderTypes(state, rand, data);
        }
        return ChunkRenderTypeSet.of(RenderType.solid());
    }

    public BakedModel[] getModels() {
        return models;
    }

    public ModelSelector getSelector() {
        return selector;
    }

    public DynamicModelEffects getEffects() {
        return effects;
    }
}