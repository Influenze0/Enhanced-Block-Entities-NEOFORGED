package foundationgames.enhancedblockentities.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import foundationgames.enhancedblockentities.event.EBEEvents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

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