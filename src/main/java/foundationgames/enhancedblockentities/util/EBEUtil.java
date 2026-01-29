package foundationgames.enhancedblockentities.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.io.IOException;
import java.nio.file.Files;

public enum EBEUtil {
    ;

    private static final RandomSource RANDOM = RandomSource.create();

    public static final DyeColor[] DEFAULTED_DYE_COLORS;
    public static final Direction[] HORIZONTAL_DIRECTIONS;

    static {
        var dColors = DyeColor.values();
        DEFAULTED_DYE_COLORS = new DyeColor[dColors.length + 1];
        System.arraycopy(dColors, 0, DEFAULTED_DYE_COLORS, 0, dColors.length);

        HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    }

    public static void renderBakedModel(MultiBufferSource bufferSource, BlockState state,
                                        PoseStack poseStack, BakedModel model,
                                        int light, int overlay) {
        VertexConsumer vertices = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(state, false));
        for (int i = 0; i <= 6; i++) {
            Direction dir = i == 0 ? null : Direction.values()[i - 1];
            for (BakedQuad quad : model.getQuads(state, dir, RANDOM, ModelData.EMPTY, null)) {
                vertices.putBulkData(poseStack.last(), quad, 1.0f, 1.0f, 1.0f, 1.0f, light, overlay);
            }
        }
    }

    public static void renderBakedModelColored(MultiBufferSource bufferSource, BlockState state,
                                               PoseStack poseStack, BakedModel model,
                                               float red, float green, float blue, float alpha,
                                               int light, int overlay) {
        VertexConsumer vertices = bufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(state, false));

        for (int i = 0; i <= 6; i++) {
            Direction dir = i == 0 ? null : Direction.values()[i - 1];
            for (BakedQuad quad : model.getQuads(state, dir, RANDOM, ModelData.EMPTY, null)) {
                vertices.putBulkData(poseStack.last(), quad, red, green, blue, alpha, light, overlay);
            }
        }
    }

    public static boolean isVanillaResourcePack(PackResources pack) {
        return pack instanceof VanillaPackResources;
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(EnhancedBlockEntities.MOD_ID, path);
    }

    public static final String DUMP_FOLDER_NAME = "enhanced_bes_dump";

    public static void dumpResources() throws IOException {
        var path = FMLPaths.GAMEDIR.get().resolve(DUMP_FOLDER_NAME);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        ResourceUtil.dumpAllPacks(path);
    }

    public static Direction getDirectionFromIndex(int index) {
        if (index < 0 || index >= Direction.values().length) {
            return Direction.NORTH;
        }
        return Direction.values()[index];
    }
}