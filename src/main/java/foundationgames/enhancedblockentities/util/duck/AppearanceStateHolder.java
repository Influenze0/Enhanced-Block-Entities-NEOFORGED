package foundationgames.enhancedblockentities.util.duck;

import foundationgames.enhancedblockentities.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface AppearanceStateHolder {
    int getModelState();

    void setModelState(int state);

    int getRenderState();

    void setRenderState(int state);

    default void updateAppearanceState(int state, Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            return;
        }

        this.setModelState(state);
        WorldUtil.rebuildChunkAndThen(level, pos, () -> this.setRenderState(state));
    }
}