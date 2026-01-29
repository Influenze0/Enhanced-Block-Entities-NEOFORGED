package foundationgames.enhancedblockentities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import foundationgames.enhancedblockentities.EnhancedBlockEntityRegistry;
import foundationgames.enhancedblockentities.client.render.BlockEntityRenderCondition;
import foundationgames.enhancedblockentities.client.render.BlockEntityRendererOverride;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {

    @Inject(
            method = "setupAndRender",
            at = @At("HEAD"),
            cancellable = true
    )
    private static <T extends BlockEntity> void enhanced_bes$renderOverrides(
            BlockEntityRenderer<T> renderer,
            T blockEntity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            CallbackInfo ci
    ) {
        if (EnhancedBlockEntityRegistry.ENTITIES.containsKey(blockEntity.getType())
                && EnhancedBlockEntityRegistry.BLOCKS.contains(blockEntity.getBlockState().getBlock())) {

            Tuple<BlockEntityRenderCondition, BlockEntityRendererOverride> entry =
                    EnhancedBlockEntityRegistry.ENTITIES.get(blockEntity.getType());

            if (entry.getA().shouldRender(blockEntity)) {
                int packedLight = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos());
                entry.getB().render(
                        (BlockEntityRenderer<?>) renderer,
                        blockEntity,
                        partialTick,
                        poseStack,
                        bufferSource,
                        packedLight,
                        OverlayTexture.NO_OVERLAY
                );
            }
            ci.cancel();
        }
    }
}