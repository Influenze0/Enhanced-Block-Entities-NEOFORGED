package foundationgames.enhancedblockentities;

import foundationgames.enhancedblockentities.client.render.BlockEntityRenderCondition;
import foundationgames.enhancedblockentities.client.render.BlockEntityRendererOverride;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnhancedBlockEntityRegistry {
    public static final Map<BlockEntityType<?>, Tuple<BlockEntityRenderCondition, BlockEntityRendererOverride>> ENTITIES = new HashMap<>();
    public static final Set<Block> BLOCKS = new HashSet<>();

    public static void register(Block block, BlockEntityType<?> type,
                                BlockEntityRenderCondition condition,
                                BlockEntityRendererOverride override) {
        BLOCKS.add(block);
        ENTITIES.put(type, new Tuple<>(condition, override));
    }

    public static void clear() {
        ENTITIES.clear();
        BLOCKS.clear();
    }
}