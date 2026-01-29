package foundationgames.enhancedblockentities;

import foundationgames.enhancedblockentities.client.model.ModelIdentifiers;
import foundationgames.enhancedblockentities.client.model.ModelRegistrationHandler;
import foundationgames.enhancedblockentities.client.resource.template.TemplateLoader;
import foundationgames.enhancedblockentities.config.EBEConfig;
import foundationgames.enhancedblockentities.util.DateUtil;
import foundationgames.enhancedblockentities.util.EBEUtil;
import foundationgames.enhancedblockentities.util.ResourceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EnhancedBlockEntities.MOD_ID)
public final class EnhancedBlockEntities {
    public static final String MOD_ID = "enhancedblockentities";
    public static final String NAMESPACE = "ebe";
    public static final Logger LOG = LogManager.getLogger("Enhanced Block Entities");
    public static final EBEConfig CONFIG = new EBEConfig();

    public static final TemplateLoader TEMPLATE_LOADER = new TemplateLoader();

    private static boolean initialized = false;
    private static boolean earlyLoaded = false;

    public EnhancedBlockEntities(IEventBus modEventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            initClient(modEventBus);
        }
    }

    private void initClient(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onRegisterReloadListeners);

        ResourceUtil.ensureInitialized();
        ModelIdentifiers.init();
        EBESetup.setupResourceProviders();

        var config = CONFIG;
        if (config.renderEnhancedChests) {
            EBESetup.setupChests();
        }
        if (config.renderEnhancedSigns) {
            EBESetup.setupSigns();
        }
        if (config.renderEnhancedBeds) {
            EBESetup.setupBeds();
        }
        if (config.renderEnhancedShulkerBoxes) {
            EBESetup.setupShulkerBoxes();
        }
    }

    private static void fillResourcePacks() {
        if (CONFIG.renderEnhancedChests) {
            EBESetup.setupChests();
            EBESetup.setupRRPChests();
        }

        if (CONFIG.renderEnhancedSigns) {
            EBESetup.setupSigns();
            EBESetup.setupRRPSigns();
        }

        if (CONFIG.renderEnhancedBeds) {
            EBESetup.setupBeds();
            EBESetup.setupRRPBeds();
        }

        if (CONFIG.renderEnhancedShulkerBoxes) {
            EBESetup.setupShulkerBoxes();
            EBESetup.setupRRPShulkerBoxes();
        }
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> initialized = true);
    }

    private void onRegisterReloadListeners(AddClientReloadListenersEvent event) {
    }

    public static void reload(ReloadType type) {
        if (!initialized) {
            return;
        }

        load();
        Minecraft mc = Minecraft.getInstance();

        if (type == ReloadType.WORLD && mc.levelRenderer != null) {
            mc.levelRenderer.allChanged();
        } else if (type == ReloadType.RESOURCES) {
            mc.reloadResourcePacks();
        }
    }

    public static void load() {
        CONFIG.load();

        EnhancedBlockEntityRegistry.clear();
        ModelRegistrationHandler.clearPlugins();
        ResourceUtil.resetBasePack();
        ResourceUtil.resetTopLevelPack();

        if (CONFIG.renderEnhancedChests) {
            EBESetup.setupChests();
            EBESetup.setupRRPChests();
        }

        if (CONFIG.renderEnhancedSigns) {
            EBESetup.setupSigns();
            EBESetup.setupRRPSigns();
        }

        if (CONFIG.renderEnhancedBeds) {
            EBESetup.setupBeds();
            EBESetup.setupRRPBeds();
        }

        if (CONFIG.renderEnhancedShulkerBoxes) {
            EBESetup.setupShulkerBoxes();
            EBESetup.setupRRPShulkerBoxes();
        }

        EBESetup.setupResourceProviders();
    }
}