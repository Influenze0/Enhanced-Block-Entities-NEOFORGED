package foundationgames.enhancedblockentities;

import foundationgames.enhancedblockentities.client.render.SignRenderManager;
import foundationgames.enhancedblockentities.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = EnhancedBlockEntities.MOD_ID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            SignRenderManager.endFrame();
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            WorldUtil.EVENT_LISTENER.onEndTick(mc.level);
        }
    }
}