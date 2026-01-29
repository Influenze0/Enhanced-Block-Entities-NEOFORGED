package foundationgames.enhancedblockentities.mixin;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.client.resource.EBEPack;
import foundationgames.enhancedblockentities.util.EBEUtil;
import foundationgames.enhancedblockentities.util.ResourceUtil;
import foundationgames.enhancedblockentities.util.hacks.ExperimentalSetup;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(MultiPackResourceManager.class)
public abstract class LifecycledResourceManagerImplMixin {

    @Shadow
    @Final
    private Map<String, FallbackResourceManager> namespacedManagers;

    @ModifyVariable(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;copyOf(Ljava/util/Collection;)Ljava/util/List;",
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0
    )
    private List<PackResources> enhanced_bes$injectBasePack(List<PackResources> old) {
        ResourceUtil.ensureInitialized();

        var packs = new ArrayList<>(old);

        int idx = 0;
        if (!packs.isEmpty()) {
            do {
                idx++;
            } while (idx < packs.size() && !EBEUtil.isVanillaResourcePack(packs.get(idx - 1)));
        }

        EBEPack basePack = ResourceUtil.getBasePack();
        packs.add(idx, basePack);

        return packs;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void enhanced_bes$injectTopLevelPack(PackType type, List<PackResources> packs, CallbackInfo ci) {
        ExperimentalSetup.cacheResources((ResourceManager) this);
        ExperimentalSetup.setup();

        addPack(type, ResourceUtil.getTopLevelPack());
    }

    private void addPack(PackType type, PackResources pack) {
        for (var namespace : pack.getNamespaces(type)) {
            this.namespacedManagers.computeIfAbsent(
                    namespace,
                    n -> new FallbackResourceManager(type, n)
            ).push(pack);
        }
    }
}