package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.util.WorldUtil;
import foundationgames.enhancedblockentities.util.duck.ChunkRebuildTaskAccess;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin {

    @ModifyVariable(method = "compileSections",
            at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"),
            index = 7)
    private SectionRenderDispatcher.RenderSection enhanced_bes$addPostRebuildTask(SectionRenderDispatcher.RenderSection section) {
        if (WorldUtil.CHUNK_UPDATE_TASKS.size() > 0) {
            var pos = SectionPos.of(section.getOrigin());

            if (WorldUtil.CHUNK_UPDATE_TASKS.containsKey(pos)) {
                var task = WorldUtil.CHUNK_UPDATE_TASKS.remove(pos);
                ((ChunkRebuildTaskAccess) section).enhanced_bes$setTaskAfterRebuild(task);
            }
        }

        return section;
    }

    @Inject(method = "addRecentlyCompiledSection", at = @At("HEAD"))
    private void enhanced_bes$runPostRebuildTask(SectionRenderDispatcher.RenderSection section, CallbackInfo ci) {
        ((ChunkRebuildTaskAccess) section).enhanced_bes$runAfterRebuildTask();
    }
}