package foundationgames.enhancedblockentities.client.resource;

import com.google.gson.JsonObject;
import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.client.resource.template.TemplateLoader;
import foundationgames.enhancedblockentities.client.resource.template.TemplateProvider;
import net.minecraft.SharedConstants;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class EBEPack implements PackResources {
    public static final ResourceLocation BLOCK_ATLAS = ResourceLocation.withDefaultNamespace("blocks");

    private final Map<ResourceLocation, AtlasResourceBuilder> atlases = new HashMap<>();
    private final Map<ResourceLocation, IoSupplier<InputStream>> resources = new HashMap<>();
    private final Set<String> namespaces = new HashSet<>();

    private final TemplateLoader templates;

    private final JsonObject packMeta;
    private final PackLocationInfo packInfo;

    public EBEPack(ResourceLocation id, TemplateLoader templates) {
        this.templates = templates;

        this.packMeta = new JsonObject();
        this.packMeta.addProperty("pack_format", SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));
        this.packMeta.addProperty("description", "Enhanced Block Entities Resources");

        this.packInfo = new PackLocationInfo(id.toString(), Component.literal(id.toString()), PackSource.BUILT_IN, Optional.empty());
    }

    public void addAtlasSprite(ResourceLocation atlas, SpriteSource source) {
        var resource = this.atlases.computeIfAbsent(atlas, id -> new AtlasResourceBuilder());
        resource.put(source);

        this.addResource(ResourceLocation.fromNamespaceAndPath(atlas.getNamespace(), "atlases/" + atlas.getPath() + ".json"), resource::toBytes);
    }

    public void addSingleBlockSprite(ResourceLocation path) {
        this.addAtlasSprite(BLOCK_ATLAS, new SingleFile(path, Optional.empty()));
    }

    public void addDirBlockSprites(String dir, String prefix) {
        this.addAtlasSprite(BLOCK_ATLAS, new DirectoryLister(dir, prefix));
    }

    public void addResource(ResourceLocation id, IoSupplier<byte[]> resource) {
        this.namespaces.add(id.getNamespace());
        this.resources.put(id, new LazyBufferedResource(resource));
    }

    public void addResource(ResourceLocation id, byte[] resource) {
        this.namespaces.add(id.getNamespace());
        this.resources.put(id, () -> new ByteArrayInputStream(resource));
    }

    public void addPlainTextResource(ResourceLocation id, String plainText) {
        this.addResource(id, plainText.getBytes(StandardCharsets.UTF_8));
    }

    public void addTemplateResource(ResourceLocation id, TemplateProvider.TemplateApplyingFunction template) {
        this.addResource(id, () -> template.getAndApplyTemplate(new TemplateProvider(this.templates)).getBytes(StandardCharsets.UTF_8));
    }

    public void addTemplateResource(ResourceLocation id, String templatePath) {
        this.addTemplateResource(id, t -> t.load(templatePath, d -> {}));
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... segments) {
        return null; // Provide no root resources
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation id) {
        if (type != PackType.CLIENT_RESOURCES) return null;

        return this.resources.get(id);
    }

    @Override
    public void listResources(PackType type, String namespace, String prefix, ResourceOutput consumer) {
        if (type != PackType.CLIENT_RESOURCES) return;

        for (var entry : this.resources.entrySet()) {
            var id = entry.getKey();

            if (id.getNamespace().startsWith(namespace) && id.getPath().startsWith(prefix)) {
                consumer.accept(id, entry.getValue());
            }
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        if (type != PackType.CLIENT_RESOURCES) return Set.of();

        return this.namespaces;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> meta) throws IOException {
        if ("pack".equals(meta.getMetadataSectionName())) {
            return meta.fromJson(this.packMeta);
        }

        return null;
    }

    @Override
    public PackLocationInfo location() {
        return this.packInfo;
    }

    @Override
    public String packId() {
        return this.packInfo.id();
    }

    @Override
    public void close() {
    }

    public void dump(Path dir) throws IOException {
        dir = dir.resolve("assets");

        for (var entry : this.resources.entrySet()) {
            var id = entry.getKey();
            var file = dir.resolve(id.getNamespace()).resolve(id.getPath());

            Files.createDirectories(file.getParent());

            try (var out = Files.newOutputStream(file)) {
                var in = entry.getValue().get();

                int i;
                while ((i = in.read()) >= 0) {
                    out.write(i);
                }
            }
        }
    }

    public static class PropertyBuilder {
        private Properties properties = new Properties();

        private PropertyBuilder() {}

        public PropertyBuilder def(String k, String v) {
            if (this.properties != null) {
                this.properties.setProperty(k, v);
            }

            return this;
        }

        private Properties build() {
            var properties = this.properties;
            this.properties = null;

            return properties;
        }
    }

    public static class LazyBufferedResource implements IoSupplier<InputStream> {
        private final IoSupplier<byte[]> backing;
        private byte[] buffer = null;

        public LazyBufferedResource(IoSupplier<byte[]> backing) {
            this.backing = backing;
        }

        @Override
        public InputStream get() throws IOException {
            if (buffer == null) {
                buffer = backing.get();
            }

            return new ByteArrayInputStream(buffer);
        }
    }
}