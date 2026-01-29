package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.util.duck.ChunkRebuildTaskAccess;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SectionRenderDispatcher.RenderSection.class)
public class BuiltChunkMixin implements ChunkRebuildTaskAccess {
    @Unique
    private @Nullable Runnable enhanced_bes$taskAfterRebuild = null;

    @Override
    public Runnable enhanced_bes$getTaskAfterRebuild() {
        return enhanced_bes$taskAfterRebuild;
    }

    @Override
    public void enhanced_bes$setTaskAfterRebuild(Runnable task) {
        enhanced_bes$taskAfterRebuild = task;
    }
}