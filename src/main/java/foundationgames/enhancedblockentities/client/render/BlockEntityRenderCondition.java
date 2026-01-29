package foundationgames.enhancedblockentities.client.render;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.config.EBEConfig;
import foundationgames.enhancedblockentities.mixin.SignBlockEntityRenderAccessor;
import foundationgames.enhancedblockentities.util.duck.AppearanceStateHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface BlockEntityRenderCondition {
    BlockEntityRenderCondition NON_ZERO_STATE = entity -> {
        if (entity instanceof AppearanceStateHolder stateHolder) {
            return stateHolder.getRenderState() > 0;
        }
        return false;
    };

    BlockEntityRenderCondition CHEST = NON_ZERO_STATE;

    BlockEntityRenderCondition SHULKER_BOX = NON_ZERO_STATE;

    BlockEntityRenderCondition SIGN = entity -> {
        EBEConfig config = EnhancedBlockEntities.CONFIG;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return false;
        }

        if (config.signTextRendering.equals("all")) {
            return true;
        }

        double playerDistance = mc.player.blockPosition().distSqr(entity.getBlockPos());

        if (config.signTextRendering.equals("smart")) {
            SignRenderManager.renderedSigns++;
            return playerDistance < 80 + Math.max(0, 580 - (SignRenderManager.getRenderedSignAmount() * 0.7));
        }

        int distSquared = SignBlockEntityRenderAccessor.enhanced_bes$getRenderDistance();
        double dist = Math.sqrt(distSquared); // OUTLINE_RENDER_DISTANCE = Mth.square(16) = 256

        Vec3 blockPos = Vec3.atCenterOf(entity.getBlockPos());
        Vec3 playerPos = mc.player.position();

        if (config.signTextRendering.equals("most")) {
            return blockPos.closerThan(playerPos, dist * 0.6);
        }
        if (config.signTextRendering.equals("some")) {
            return blockPos.closerThan(playerPos, dist * 0.3);
        }
        if (config.signTextRendering.equals("few")) {
            return blockPos.closerThan(playerPos, dist * 0.15);
        }
        return false;
    };

    BlockEntityRenderCondition NEVER = entity -> false;

    BlockEntityRenderCondition ALWAYS = entity -> true;

    boolean shouldRender(BlockEntity entity);
}