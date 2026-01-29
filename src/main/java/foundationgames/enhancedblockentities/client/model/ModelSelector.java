package foundationgames.enhancedblockentities.client.model;

import foundationgames.enhancedblockentities.util.DateUtil;
import foundationgames.enhancedblockentities.util.duck.AppearanceStateHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelSelector {
    private static final List<ModelSelector> REGISTRY = new ArrayList<>();


    public static final ModelProperty<int[]> MODEL_INDICES_PROPERTY = new ModelProperty<>();

    public static final ModelSelector STATE_HOLDER_SELECTOR = new ModelSelector() {
        @Override
        public void writeModelIndices(BlockAndTintGetter level, BlockState state, BlockPos pos, int[] indices) {
            if (level.getBlockEntity(pos) instanceof AppearanceStateHolder stateHolder) {
                indices[0] = stateHolder.getModelState();
                return;
            }
            indices[0] = 0;
        }

        @Override
        public void writeModelIndicesWithoutContext(@Nullable BlockState state, RandomSource random, int[] indices) {
            indices[0] = 0;
        }
    };

    public static final ModelSelector CHEST = STATE_HOLDER_SELECTOR;

    public static final ModelSelector CHEST_WITH_CHRISTMAS = new ModelSelector() {
        @Override
        public int getParticleModelIndex() {
            return DateUtil.isChristmas() ? 2 : 0;
        }

        @Override
        public void writeModelIndices(BlockAndTintGetter level, BlockState state, BlockPos pos, int[] indices) {
            if (level.getBlockEntity(pos) instanceof AppearanceStateHolder stateHolder) {
                indices[0] = stateHolder.getModelState() + this.getParticleModelIndex();
                return;
            }
            indices[0] = this.getParticleModelIndex();
        }

        @Override
        public void writeModelIndicesWithoutContext(@Nullable BlockState state, RandomSource random, int[] indices) {
            indices[0] = this.getParticleModelIndex();
        }
    };

    public static final ModelSelector SHULKER_BOX = STATE_HOLDER_SELECTOR;

    public final int id;
    public final int displayedModelCount;

    public ModelSelector(int displayedModelCount) {
        this.id = REGISTRY.size();
        this.displayedModelCount = displayedModelCount;
        REGISTRY.add(this);
    }

    public ModelSelector() {
        this(1);
    }

    public int getParticleModelIndex() {
        return 0;
    }


    public int displayedModelCount() {
        return displayedModelCount;
    }

    public abstract void writeModelIndices(BlockAndTintGetter level, BlockState state, BlockPos pos, int[] indices);
    public abstract void writeModelIndicesWithoutContext(@Nullable BlockState state, RandomSource random, int[] indices);

    public static ModelSelector fromId(int id) {
        if (id >= 0 && id < REGISTRY.size()) {
            return REGISTRY.get(id);
        }
        return STATE_HOLDER_SELECTOR;
    }

   
    public static int getRegistrySize() {
        return REGISTRY.size();
    }
}