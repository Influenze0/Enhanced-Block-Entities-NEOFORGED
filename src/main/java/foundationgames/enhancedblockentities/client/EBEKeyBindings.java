package foundationgames.enhancedblockentities.client;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.config.gui.screen.EBEConfigScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = EnhancedBlockEntities.MOD_ID)
public class EBEKeyBindings {
    public static final String CATEGORY = "key.categories.ebe";
    public static final String OPEN_CONFIG_KEY = "key.ebe.open_config";

    public static KeyMapping OPEN_CONFIG;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        OPEN_CONFIG = new KeyMapping(
                OPEN_CONFIG_KEY,
                GLFW.GLFW_KEY_UNKNOWN,
                CATEGORY
        );
        event.register(OPEN_CONFIG);
    }

    @EventBusSubscriber(modid = EnhancedBlockEntities.MOD_ID)
    public static class KeyHandler {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.screen != null) return;

            if (OPEN_CONFIG.consumeClick()) {
                mc.setScreen(new EBEConfigScreen(mc.screen));
            }
        }
    }
}
