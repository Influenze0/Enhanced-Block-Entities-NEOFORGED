package foundationgames.enhancedblockentities.client.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AtlasResourceBuilder {
    private static final Gson GSON = new Gson();
    private final List<SpriteSource> sources = new ArrayList<>();

    public void put(SpriteSource source) {
        sources.add(source);
    }

    public byte[] toBytes() {
        return GSON.toJson(SpriteSources.FILE_CODEC.encode(this.sources, JsonOps.INSTANCE, new JsonObject())
                        .getOrThrow())
                .getBytes(StandardCharsets.UTF_8);
    }
}