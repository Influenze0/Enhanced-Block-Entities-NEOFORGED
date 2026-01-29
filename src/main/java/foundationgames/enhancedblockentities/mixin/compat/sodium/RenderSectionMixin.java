package foundationgames.enhancedblockentities.mixin.compat.sodium;

import foundationgames.enhancedblockentities.util.duck.ChunkRebuildTaskAccess;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSection;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

@Pseudo
@Mixin(value = RenderSection.class, remap = false)
public class RenderSectionMixin implements ChunkRebuildTaskAccess {
    private @Unique @Nullable Runnable enhanced_bes$taskAfterRebuild = null;

    @Override
    public Runnable enhanced_bes$getTaskAfterRebuild() {
        return enhanced_bes$taskAfterRebuild;
    }

    @Override
    public void enhanced_bes$setTaskAfterRebuild(Runnable task) {
        enhanced_bes$taskAfterRebuild = task;
    }
}
