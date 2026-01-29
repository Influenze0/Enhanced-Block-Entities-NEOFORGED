package foundationgames.enhancedblockentities.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import foundationgames.enhancedblockentities.client.render.BlockEntityRendererOverride;
import foundationgames.enhancedblockentities.util.EBEUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ShulkerBoxBlockEntityRendererOverride extends BlockEntityRendererOverride {
    private final Map<DyeColor, BakedModel> models = new HashMap<>();
    private final Consumer<Map<DyeColor, BakedModel>> modelMapFiller;

    public ShulkerBoxBlockEntityRendererOverride(Consumer<Map<DyeColor, BakedModel>> modelMapFiller) {
        this.modelMapFiller = modelMapFiller;
    }

    @Override
    public void render(BlockEntityRenderer<?> renderer, BlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (models.isEmpty()) modelMapFiller.accept(models);
        if (blockEntity instanceof ShulkerBoxBlockEntity entity) {
            Direction dir = Direction.UP;
            BlockState state = entity.getLevel().getBlockState(entity.getBlockPos());
            if (state.getBlock() instanceof ShulkerBoxBlock) {
                dir = state.getValue(ShulkerBoxBlock.FACING);
            }
            poseStack.pushPose();

            float animation = entity.getProgress(tickDelta);

            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(dir.getRotation());
            poseStack.mulPose(Axis.YP.rotationDegrees(270 * animation));
            poseStack.translate(-0.5, -0.5, -0.5);

            poseStack.translate(0, animation * 0.5f, 0);

            var lidModel = models.get(entity.getColor());
            if (lidModel != null) {
                EBEUtil.renderBakedModel(bufferSource, blockEntity.getBlockState(), poseStack, lidModel, light, overlay);
            }

            poseStack.popPose();
        }
    }

    @Override
    public void onModelsReload() {
        this.models.clear();
    }
}