package foundationgames.enhancedblockentities.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import foundationgames.enhancedblockentities.client.render.BlockEntityRendererOverride;
import foundationgames.enhancedblockentities.mixin.SignBlockEntityRenderAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class SignBlockEntityRendererOverride extends BlockEntityRendererOverride {

    public SignBlockEntityRendererOverride() {
    }

    @Override
    public void render(BlockEntityRenderer<?> renderer, BlockEntity blockEntity, float tickDelta,
                       PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (blockEntity instanceof SignBlockEntity signEntity) {
            var state = signEntity.getBlockState();
            SignBlock block = (SignBlock) state.getBlock();
            var textRenderer = (SignBlockEntityRenderAccessor) renderer;

            textRenderer.enhanced_bes$setAngles(poseStack, -block.getYRotationDegrees(state), state);
            textRenderer.enhanced_bes$renderText(
                    signEntity.getBlockPos(),
                    signEntity.getFrontText(),
                    poseStack,
                    bufferSource,
                    light,
                    signEntity.getTextLineHeight(),
                    signEntity.getMaxTextLineWidth(),
                    true
            );
            textRenderer.enhanced_bes$renderText(
                    signEntity.getBlockPos(),
                    signEntity.getBackText(),
                    poseStack,
                    bufferSource,
                    light,
                    signEntity.getTextLineHeight(),
                    signEntity.getMaxTextLineWidth(),
                    false
            );
        }
    }
}