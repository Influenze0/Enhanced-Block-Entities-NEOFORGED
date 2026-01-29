package foundationgames.enhancedblockentities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HangingSignRenderer.class)
public interface HangingSignRendererAccessor {

    @Invoker("translateSign")
    void enhanced_bes$setAngles(PoseStack poseStack, float rotation, BlockState state);
}
