package foundationgames.enhancedblockentities.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public enum WorldUtil {
    EVENT_LISTENER;

    public static final Map<SectionPos, ExecutableRunnableHashSet> CHUNK_UPDATE_TASKS = new HashMap<>();
    private static final Map<ResourceKey<Level>, Long2ObjectMap<Runnable>> TIMED_TASKS = new HashMap<>();

    public static void rebuildChunk(Level level, BlockPos pos) {
        var state = level.getBlockState(pos);
        Minecraft.getInstance().levelRenderer.blockChanged(level, pos, state, state, 8);
    }

    public static void rebuildChunkAndThen(Level level, BlockPos pos, Runnable action) {
        CHUNK_UPDATE_TASKS.computeIfAbsent(SectionPos.of(pos), k -> new ExecutableRunnableHashSet()).add(action);
        rebuildChunk(level, pos);
    }

    public static void scheduleTimed(Level level, long time, Runnable action) {
        TIMED_TASKS.computeIfAbsent(level.dimension(), k -> new Long2ObjectOpenHashMap<>()).put(time, action);
    }

    public void onEndTick(ClientLevel level) {
        var key = level.dimension();

        if (TIMED_TASKS.containsKey(key)) {
            TIMED_TASKS.get(key).long2ObjectEntrySet().removeIf(entry -> {
                if (level.getGameTime() >= entry.getLongKey()) {
                    entry.getValue().run();
                    return true;
                }

                return false;
            });
        }
    }
}