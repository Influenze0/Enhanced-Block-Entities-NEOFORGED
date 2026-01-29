package foundationgames.enhancedblockentities.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import foundationgames.enhancedblockentities.client.render.BlockEntityRendererOverride;
import foundationgames.enhancedblockentities.util.EBEUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.function.Function;
import java.util.function.Supplier;

public class ChestBlockEntityRendererOverride extends BlockEntityRendererOverride {
    private BakedModel[] models = null;
    private final Supplier<BakedModel[]> modelGetter;
    private final Function<BlockEntity, Integer> modelSelector;

    public ChestBlockEntityRendererOverride(Supplier<BakedModel[]> modelGetter, Function<BlockEntity, Integer> modelSelector) {
        this.modelGetter = modelGetter;
        this.modelSelector = modelSelector;
    }

    @Override
    public void render(BlockEntityRenderer<?> renderer, BlockEntity blockEntity, float tickDelta,
                       PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (models == null) {
            models = modelGetter.get();
        }

        if (blockEntity instanceof LidBlockEntity) {
            poseStack.pushPose();

            LidBlockEntity chest = getLidAnimationHolder(blockEntity, tickDelta);
            poseStack.translate(0.5f, 0, 0.5f);

            Direction dir = blockEntity.getBlockState().getValue(ChestBlock.FACING);
            poseStack.mulPose(Axis.YP.rotationDegrees(180 - dir.toYRot()));
            poseStack.translate(-0.5f, 0, -0.5f);

            float yPiv = 9f / 16;
            float zPiv = 15f / 16;
            poseStack.translate(0, yPiv, zPiv);

            float rot = chest.getOpenNess(tickDelta);
            rot = 1f - rot;
            rot = 1f - (rot * rot * rot);
            poseStack.mulPose(Axis.XP.rotationDegrees(rot * 90));
            poseStack.translate(0, -yPiv, -zPiv);

            EBEUtil.renderBakedModel(
                    bufferSource,
                    blockEntity.getBlockState(),
                    poseStack,
                    models[modelSelector.apply(blockEntity)],
                    light,
                    overlay
            );

            poseStack.popPose();
        }
    }

    public static LidBlockEntity getLidAnimationHolder(BlockEntity blockEntity, float tickDelta) {
        LidBlockEntity chest = (LidBlockEntity) blockEntity;

        BlockState state = blockEntity.getBlockState();
        if (state.hasProperty(ChestBlock.TYPE) && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
            BlockEntity neighbor = null;
            BlockPos pos = blockEntity.getBlockPos();
            Direction facing = state.getValue(ChestBlock.FACING);

            switch (state.getValue(ChestBlock.TYPE)) {
                case LEFT -> neighbor = blockEntity.getLevel().getBlockEntity(pos.relative(facing.getClockWise()));
                case RIGHT -> neighbor = blockEntity.getLevel().getBlockEntity(pos.relative(facing.getCounterClockWise()));
            }

            if (neighbor instanceof LidBlockEntity lidNeighbor) {
                float nAnim = lidNeighbor.getOpenNess(tickDelta);
                if (nAnim > chest.getOpenNess(tickDelta)) {
                    chest = lidNeighbor;
                }
            }
        }

        return chest;
    }

    @Override
    public void onModelsReload() {
        this.models = null;
    }
}