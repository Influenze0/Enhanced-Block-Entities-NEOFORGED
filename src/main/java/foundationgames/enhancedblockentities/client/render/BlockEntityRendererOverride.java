package foundationgames.enhancedblockentities.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import foundationgames.enhancedblockentities.event.EBEEvents;
import foundationgames.enhancedblockentities.mixin.AbstractSignRendererAccessor;
import foundationgames.enhancedblockentities.mixin.HangingSignRendererAccessor;
import foundationgames.enhancedblockentities.mixin.SignBlockEntityRenderAccessor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class BlockEntityRendererOverride {
    private static final class NoOpOverride extends BlockEntityRendererOverride {
        private NoOpOverride() {
            super(false);
        }

        @Override
        public void render(BlockEntityRenderer<?> renderer, BlockEntity blockEntity, float tickDelta,
                           PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        }

        @Override
        public void onModelsReload() {
        }
    }

    public static final BlockEntityRendererOverride NO_OP = new NoOpOverride();

    private static final class DelegateOverride extends BlockEntityRendererOverride {
        private DelegateOverride() {
            super(false);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void render(BlockEntityRenderer<?> renderer, BlockEntity blockEntity, float tickDelta,
                           PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
            ((BlockEntityRenderer<BlockEntity>) renderer).render(blockEntity, tickDelta, poseStack, bufferSource, light, overlay);
        }
    }

    public static final BlockEntityRendererOverride DELEGATE = new DelegateOverride();

    private static final class SignTextOnlyOverride extends BlockEntityRendererOverride {
        private static final int LINE_HEIGHT = 9;
        private static final int MAX_WIDTH = 90;

        private SignTextOnlyOverride() {
            super(false);
        }

        @Override
        public void render(BlockEntityRenderer<?> renderer, BlockEntity blockEntity, float tickDelta,
                           PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
            if (!(blockEntity instanceof SignBlockEntity sign) || !(renderer instanceof AbstractSignRendererAccessor textRenderer)) {
                return;
            }
            BlockState state = sign.getBlockState();
            var pos = sign.getBlockPos();

            float rotation = getSignRotation(state);
            poseStack.pushPose();
            if (renderer instanceof SignBlockEntityRenderAccessor signAccessor) {
                signAccessor.enhanced_bes$setAngles(poseStack, rotation, state);
            } else if (renderer instanceof HangingSignRendererAccessor hangingAccessor) {
                hangingAccessor.enhanced_bes$setAngles(poseStack, rotation, state);
            } else {
                poseStack.popPose();
                return;
            }
            textRenderer.enhanced_bes$renderText(pos, sign.getFrontText(), poseStack, bufferSource, light, LINE_HEIGHT, MAX_WIDTH, true);
            textRenderer.enhanced_bes$renderText(pos, sign.getBackText(), poseStack, bufferSource, light, LINE_HEIGHT, MAX_WIDTH, false);
            poseStack.popPose();
        }

        private static float getSignRotation(BlockState state) {
            if (state.hasProperty(BlockStateProperties.ROTATION_16)) {
                return state.getValue(BlockStateProperties.ROTATION_16) * 22.5f;
            }
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                return state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
            }
            return 0f;
        }
    }

    public static final BlockEntityRendererOverride SIGN_TEXT_ONLY = new SignTextOnlyOverride();

    public BlockEntityRendererOverride() {
        this(true);
    }

    protected BlockEntityRendererOverride(boolean registerReloadEvent) {
        if (registerReloadEvent) {
            EBEEvents.registerResourceReload(this::onModelsReload);
        }
    }

    public abstract void render(
            BlockEntityRenderer<?> renderer,
            BlockEntity blockEntity,
            float tickDelta,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int light,
            int overlay
    );

    public void onModelsReload() {
    }
}