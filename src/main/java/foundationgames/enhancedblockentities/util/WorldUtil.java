package foundationgames.enhancedblockentities.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public enum WorldUtil {
    EVENT_LISTENER;

    private static final Map<ResourceKey<Level>, Long2ObjectMap<Runnable>> TIMED_TASKS = new HashMap<>();

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