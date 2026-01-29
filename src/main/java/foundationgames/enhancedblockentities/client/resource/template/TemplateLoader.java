package foundationgames.enhancedblockentities.client.resource.template;

import foundationgames.enhancedblockentities.EnhancedBlockEntities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateLoader {
    private Path rootPath;
    private final Map<String, String> loadedTemplates = new HashMap<>();

    public TemplateLoader() {
    }

    public void setRoot(Path path) {
        this.rootPath = path;
    }

    public String getOrLoadRaw(String path) throws IOException {
        if (this.loadedTemplates.containsKey(path)) {
            return this.loadedTemplates.get(path);
        }

        String templateRaw = null;
        if (this.rootPath != null) {
            var file = rootPath.resolve(path);
            if (Files.exists(file)) {
                try (var in = Files.newInputStream(file)) {
                    templateRaw = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                }
            }
        }
        if (templateRaw == null) {
            templateRaw = loadFromClasspath(path);
        }

        if (templateRaw != null) {
            this.loadedTemplates.put(path, templateRaw);
            return templateRaw;
        }

        return "";
    }

    private String loadFromClasspath(String path) {
        String[] possiblePaths = {
                "/templates/" + path,
                "/assets/" + EnhancedBlockEntities.MOD_ID + "/templates/" + path,
                "templates/" + path
        };

        for (String resourcePath : possiblePaths) {
            try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                if (is != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        return reader.lines().collect(Collectors.joining("\n"));
                    }
                }
            } catch (IOException e) {
            }
        }
        for (String resourcePath : possiblePaths) {
            String cleanPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(cleanPath)) {
                if (is != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        return reader.lines().collect(Collectors.joining("\n"));
                    }
                }
            } catch (IOException e) {
            }
        }
        return null;
    }

    public void clearCache() {
        this.loadedTemplates.clear();
    }
}