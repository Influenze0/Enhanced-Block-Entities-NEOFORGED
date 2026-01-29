package foundationgames.enhancedblockentities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSignRenderer.class)
public interface AbstractSignRendererAccessor {


    @Accessor("OUTLINE_RENDER_DISTANCE")
     static int enhanced_bes$getRenderDistance() {
         throw new AssertionError();
     }

    @Invoker("renderSignText")
    void enhanced_bes$renderText(
            BlockPos pos,
            SignText text,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int light,
            int lineHeight,
            int maxWidth,
            boolean front
    );
}