package foundationgames.enhancedblockentities.util.duck;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public interface AppearanceStateHolder {
    int getModelState();

    void setModelState(int state);

    int getRenderState();

    void setRenderState(int state);
}