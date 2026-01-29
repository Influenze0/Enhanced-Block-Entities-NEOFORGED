package foundationgames.enhancedblockentities.client.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class DynamicModelProvidingPlugin {
    private final Supplier<DynamicUnbakedModel> model;
    private final ModelResourceLocation id;

    public DynamicModelProvidingPlugin(ResourceLocation id, Supplier<DynamicUnbakedModel> model) {
        this.model = model;
        this.id = new ModelResourceLocation(id, "standalone");
    }

    public ModelResourceLocation getId() {
        return id;
    }

    public DynamicUnbakedModel getModel() {
        return model.get();
    }
}