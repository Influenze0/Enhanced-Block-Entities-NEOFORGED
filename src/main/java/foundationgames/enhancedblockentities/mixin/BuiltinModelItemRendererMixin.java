package foundationgames.enhancedblockentities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.EnhancedBlockEntityRegistry;
import foundationgames.enhancedblockentities.util.EBEUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
    private void enhanced_bes$renderBeds(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, CallbackInfo ci) {
        if (EnhancedBlockEntities.CONFIG.renderEnhancedBeds &&
                stack.getItem() instanceof BlockItem item &&
                item.getBlock() instanceof BedBlock bed &&
                EnhancedBlockEntityRegistry.BLOCKS.contains(bed)) {
            var models = Minecraft.getInstance().getModelManager().getBlockModelShaper();

            var bedState = bed.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            var footState = bedState.setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
            var footModel = models.getBlockModel(footState);
            var headState = bedState.setValue(BlockStateProperties.BED_PART, BedPart.HEAD);
            var headModel = models.getBlockModel(headState);

            poseStack.pushPose();
            EBEUtil.renderBakedModel(bufferSource, headState, poseStack, headModel, light, overlay);
            poseStack.translate(0, 0, -1);
            EBEUtil.renderBakedModel(bufferSource, footState, poseStack, footModel, light, overlay);
            poseStack.popPose();

            ci.cancel();
        }
    }
}