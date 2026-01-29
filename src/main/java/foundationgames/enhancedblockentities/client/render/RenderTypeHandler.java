package foundationgames.enhancedblockentities.client.render;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = EnhancedBlockEntities.MOD_ID)
public class RenderTypeHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            setRenderType(RenderType.cutoutMipped(),
                    Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST);

            setRenderType(RenderType.cutout(),
                    Blocks.OAK_HANGING_SIGN, Blocks.OAK_WALL_HANGING_SIGN,
                    Blocks.BIRCH_HANGING_SIGN, Blocks.BIRCH_WALL_HANGING_SIGN,
                    Blocks.SPRUCE_HANGING_SIGN, Blocks.SPRUCE_WALL_HANGING_SIGN,
                    Blocks.JUNGLE_HANGING_SIGN, Blocks.JUNGLE_WALL_HANGING_SIGN,
                    Blocks.ACACIA_HANGING_SIGN, Blocks.ACACIA_WALL_HANGING_SIGN,
                    Blocks.DARK_OAK_HANGING_SIGN, Blocks.DARK_OAK_WALL_HANGING_SIGN,
                    Blocks.MANGROVE_HANGING_SIGN, Blocks.MANGROVE_WALL_HANGING_SIGN,
                    Blocks.CHERRY_HANGING_SIGN, Blocks.CHERRY_WALL_HANGING_SIGN,
                    Blocks.CRIMSON_HANGING_SIGN, Blocks.CRIMSON_WALL_HANGING_SIGN,
                    Blocks.WARPED_HANGING_SIGN, Blocks.WARPED_WALL_HANGING_SIGN,
                    Blocks.BAMBOO_HANGING_SIGN, Blocks.BAMBOO_WALL_HANGING_SIGN);

            setRenderType(RenderType.cutoutMipped(),
                    Blocks.SHULKER_BOX,
                    Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX,
                    Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
                    Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX,
                    Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX,
                    Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX,
                    Blocks.BLACK_SHULKER_BOX);
        });
    }

    private static void setRenderType(RenderType renderType, Block... blocks) {
        for (Block block : blocks) {
            ItemBlockRenderTypes.setRenderLayer(block, renderType);
        }
    }
}