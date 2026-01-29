package foundationgames.enhancedblockentities.util;

import foundationgames.enhancedblockentities.EBESetup;
import foundationgames.enhancedblockentities.EnhancedBlockEntities;
import foundationgames.enhancedblockentities.client.resource.EBEPack;
import foundationgames.enhancedblockentities.client.resource.template.TemplateProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ResourceUtil {
    ;

    private static final String MOD_ID = EnhancedBlockEntities.MOD_ID;

    private static EBEPack BASE_PACK;
    private static EBEPack TOP_LEVEL_PACK;
    private static boolean initialized = false;

    public static synchronized void ensureInitialized() {
        if (initialized) {
            return;
        }

        EnhancedBlockEntities.CONFIG.load();
        resetBasePack();
        resetTopLevelPack();
        fillPacks();
        initialized = true;
    }

    private static void fillPacks() {
        var config = EnhancedBlockEntities.CONFIG;

        if (config.renderEnhancedChests) {
            EBESetup.setupRRPChests();
        }
        if (config.renderEnhancedSigns) {
            EBESetup.setupRRPSigns();
        }
        if (config.renderEnhancedBeds) {
            EBESetup.setupRRPBeds();
        }
        if (config.renderEnhancedShulkerBoxes) {
            EBESetup.setupRRPShulkerBoxes();
        }
    }

    public static void addChestItemModel(ResourceLocation id, String centerChest, EBEPack pack) {
        pack.addTemplateResource(id, t -> t.load("chest_item_model.json", d -> d.def("chest", MOD_ID + ":block/" + centerChest)));
    }

    private static String list(String... els) {
        return String.join(",", els);
    }

    private static String kv(String k, String v) {
        return String.format("\"" + k + "\":\"" + v + "\"");
    }

    private static String kv(String k, int v) {
        return String.format("\"" + k + "\":" + v + "");
    }

    private static String variant(TemplateProvider t, String state, String model) throws IOException {
        return t.load("blockstate/var.json", d -> d
                .def("state", state)
                .def("model", model)
                .def("extra", ""));
    }

    private static String variantY(TemplateProvider t, String state, String model, int y) throws IOException {
        return t.load("blockstate/var.json", d -> d
                .def("state", state)
                .def("model", model)
                .def("extra", kv("y", y) + ","));
    }

    private static String variantXY(TemplateProvider t, String state, String model, int x, int y) throws IOException {
        return t.load("blockstate/var.json", d -> d
                .def("state", state)
                .def("model", model)
                .def("extra", list(
                        kv("x", x),
                        kv("y", y)
                ) + ","));
    }

    private static String variantRotation16(TemplateProvider t, String keyPrefix, String modelPrefix) throws IOException {
        return list(
                variantY(t, keyPrefix + "0", modelPrefix + "_0", 180),
                variantY(t, keyPrefix + "1", modelPrefix + "_67_5", 270),
                variantY(t, keyPrefix + "2", modelPrefix + "_45", 270),
                variantY(t, keyPrefix + "3", modelPrefix + "_22_5", 270),
                variantY(t, keyPrefix + "4", modelPrefix + "_0", 270),
                variant(t, keyPrefix + "5", modelPrefix + "_67_5"),
                variant(t, keyPrefix + "6", modelPrefix + "_45"),
                variant(t, keyPrefix + "7", modelPrefix + "_22_5"),
                variant(t, keyPrefix + "8", modelPrefix + "_0"),
                variantY(t, keyPrefix + "9", modelPrefix + "_67_5", 90),
                variantY(t, keyPrefix + "10", modelPrefix + "_45", 90),
                variantY(t, keyPrefix + "11", modelPrefix + "_22_5", 90),
                variantY(t, keyPrefix + "12", modelPrefix + "_0", 90),
                variantY(t, keyPrefix + "13", modelPrefix + "_67_5", 180),
                variantY(t, keyPrefix + "14", modelPrefix + "_45", 180),
                variantY(t, keyPrefix + "15", modelPrefix + "_22_5", 180)
        );
    }

    private static String variantHFacing(TemplateProvider t, String keyPrefix, String model) throws IOException {
        return list(
                variant(t, keyPrefix + "north", model),
                variantY(t, keyPrefix + "west", model, 270),
                variantY(t, keyPrefix + "south", model, 180),
                variantY(t, keyPrefix + "east", model, 90)
        );
    }

    private static void addChestLikeModel(String parent, String chestTex, String chestName, String modelName, EBEPack pack) {
        ResourceLocation fullId = ResourceLocation.fromNamespaceAndPath(MOD_ID, "models/block/" + modelName + ".json");
        pack.addTemplateResource(fullId,
                t -> t.load("model/chest_like.json", d -> d
                        .def("parent", "minecraft:block/" + parent)
                        .def("chest_tex", chestTex)
                        .def("particle", chestParticle(chestName))
                )
        );
    }

    public static void addSingleChestModels(String chestTex, String chestName, EBEPack pack) {
        addChestLikeModel("template_chest_center", chestTex, chestName, chestName + "_center", pack);
        addChestLikeModel("template_chest_center_lid", chestTex, chestName, chestName + "_center_lid", pack);
        addChestLikeModel("template_chest_center_trunk", chestTex, chestName, chestName + "_center_trunk", pack);
    }

    public static void addDoubleChestModels(String leftTex, String rightTex, String chestName, EBEPack pack) {
        addChestLikeModel("template_chest_left", leftTex, chestName, chestName + "_left", pack);
        addChestLikeModel("template_chest_left_lid", leftTex, chestName, chestName + "_left_lid", pack);
        addChestLikeModel("template_chest_left_trunk", leftTex, chestName, chestName + "_left_trunk", pack);
        addChestLikeModel("template_chest_right", rightTex, chestName, chestName + "_right", pack);
        addChestLikeModel("template_chest_right_lid", rightTex, chestName, chestName + "_right_lid", pack);
        addChestLikeModel("template_chest_right_trunk", rightTex, chestName, chestName + "_right_trunk", pack);
    }

    private static String chestParticle(String chestName) {
        if (EnhancedBlockEntities.CONFIG.experimentalChests) {
            return kv("particle", "block/" + chestName + "_particle") + ",";
        }
        return "";
    }

    private static String bedParticle(String bedColor) {
        if (EnhancedBlockEntities.CONFIG.experimentalBeds) {
            return kv("particle", "block/" + bedColor + "_bed_particle") + ",";
        }
        return "";
    }

    private static String signParticle(String signName) {
        if (EnhancedBlockEntities.CONFIG.experimentalSigns) {
            return kv("particle", "block/" + signName + "_particle") + ",";
        }
        return "";
    }

    private static void addBlockState(ResourceLocation id, TemplateProvider.TemplateApplyingFunction vars, EBEPack pack) {
        pack.addTemplateResource(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "blockstates/" + id.getPath() + ".json"),
                t -> t.load("blockstate/base.json", d -> d.def("vars", vars)));
    }

    public static void addChestBlockStates(String chestName, EBEPack pack) {
        addBlockState(ResourceLocation.withDefaultNamespace(chestName),
                t0 -> list(
                        variantHFacing(t0, "type=single,facing=", MOD_ID + ":block/" + chestName + "_center"),
                        variantHFacing(t0, "type=left,facing=", MOD_ID + ":block/" + chestName + "_left"),
                        variantHFacing(t0, "type=right,facing=", MOD_ID + ":block/" + chestName + "_right")
                ), pack);
    }

    public static void addSingleChestOnlyBlockStates(String chestName, EBEPack pack) {
        addBlockState(ResourceLocation.withDefaultNamespace(chestName),
                t0 -> list(
                        variantHFacing(t0, "facing=", MOD_ID + ":block/" + chestName + "_center")
                ), pack);
    }

    public static void addParentModel(String parent, ResourceLocation id, EBEPack pack) {
        pack.addTemplateResource(
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "models/" + id.getPath() + ".json"),
                t -> "{" + kv("parent", parent) + "}"
        );
    }

    public static void addParentTexModel(String parent, String textures, ResourceLocation id, EBEPack pack) {
        pack.addTemplateResource(
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "models/" + id.getPath() + ".json"),
                t -> t.load("model/parent_and_tex.json", d -> d.def("parent", parent).def("textures", textures))
        );
    }

    public static void addSignTypeModels(String signType, EBEPack pack) {
        var signName = signType + "_sign";
        var signTex = "entity/signs/" + signType;
        addRotation16Models(
                signParticle(signName) + kv("sign", signTex),
                "minecraft:block/template_sign",
                "block/" + signName,
                ResourceUtil::signAOSuffix, pack);

        var hangingTexDef = list(
                kv("sign", "entity/signs/hanging/" + signType),
                kv("particle", "block/particle_hanging_sign_" + signType)
        );
        addRotation16Models(hangingTexDef,
                "minecraft:block/template_hanging_sign",
                "block/" + signType + "_hanging_sign",
                ResourceUtil::signAOSuffix, pack);
        addRotation16Models(hangingTexDef,
                "minecraft:block/template_hanging_sign_attached",
                "block/" + signType + "_hanging_sign_attached",
                ResourceUtil::signAOSuffix, pack);

        addParentTexModel(signAOSuffix("minecraft:block/template_wall_sign"),
                signParticle(signName) + kv("sign", signTex),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + signType + "_wall_sign"), pack);
        addParentTexModel(signAOSuffix("minecraft:block/template_wall_hanging_sign"),
                hangingTexDef,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + signType + "_wall_hanging_sign"), pack);
    }

    public static void addRotation16Models(String textures, String templatePrefix, String modelPath, Function<String, String> suffix, EBEPack pack) {
        addParentTexModel(suffix.apply(templatePrefix + "_0"), textures,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, modelPath + "_0"), pack);
        addParentTexModel(suffix.apply(templatePrefix + "_22_5"), textures,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, modelPath + "_22_5"), pack);
        addParentTexModel(suffix.apply(templatePrefix + "_45"), textures,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, modelPath + "_45"), pack);
        addParentTexModel(suffix.apply(templatePrefix + "_67_5"), textures,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, modelPath + "_67_5"), pack);
    }

    private static String signAOSuffix(String model) {
        if (EnhancedBlockEntities.CONFIG.signAO) {
            model += "_ao";
        }
        return model;
    }

    public static void addSignBlockStates(String signName, String wallSignName, EBEPack pack) {
        addBlockState(ResourceLocation.withDefaultNamespace(signName),
                t -> variantRotation16(t, "rotation=", MOD_ID + ":block/" + signName), pack);
        addBlockState(ResourceLocation.withDefaultNamespace(wallSignName),
                t -> variantHFacing(t, "facing=", MOD_ID + ":block/" + wallSignName), pack);
    }

    public static void addHangingSignBlockStates(String signName, String wallSignName, EBEPack pack) {
        addBlockState(ResourceLocation.withDefaultNamespace(signName),
                t -> list(
                        variantRotation16(t, "attached=false,rotation=", MOD_ID + ":block/" + signName),
                        variantRotation16(t, "attached=true,rotation=", MOD_ID + ":block/" + signName + "_attached")
                ), pack);

        addBlockState(ResourceLocation.withDefaultNamespace(wallSignName),
                t -> variantHFacing(t, "facing=", MOD_ID + ":block/" + wallSignName), pack);
    }

    public static void addBedModels(DyeColor bedColor, EBEPack pack) {
        String color = bedColor.getName();

        addParentTexModel(bedAOSuffix("minecraft:block/template_bed_head"),
                bedParticle(color) + kv("bed", "entity/bed/" + color),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + color + "_bed_head"), pack);
        addParentTexModel(bedAOSuffix("minecraft:block/template_bed_foot"),
                bedParticle(color) + kv("bed", "entity/bed/" + color),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + color + "_bed_foot"), pack);
    }

    public static void addBedBlockState(DyeColor bedColor, EBEPack pack) {
        String color = bedColor.getName();
        addBlockState(ResourceLocation.withDefaultNamespace(color + "_bed"),
                t -> {
                    var vars = new DelimitedAppender(",");
                    for (Direction dir : EBEUtil.HORIZONTAL_DIRECTIONS) {
                        int rot = (int) dir.toYRot() + 180;
                        vars
                                .append(variantY(t, "part=head,facing=" + dir.getName(),
                                        MOD_ID + ":block/" + color + "_bed_head", rot))
                                .append(variantY(t, "part=foot,facing=" + dir.getName(),
                                        MOD_ID + ":block/" + color + "_bed_foot", rot));
                    }
                    return vars.get();
                }, pack);
    }

    private static String bedAOSuffix(String model) {
        if (EnhancedBlockEntities.CONFIG.bedAO) {
            model += "_ao";
        }
        return model;
    }

    public static void addShulkerBoxModels(@Nullable DyeColor color, EBEPack pack) {
        var texture = color != null ? "entity/shulker/shulker_" + color.getName() : "entity/shulker/shulker";
        var shulkerBoxStr = color != null ? color.getName() + "_shulker_box" : "shulker_box";
        var particle = "block/" + shulkerBoxStr;

        addParentTexModel("minecraft:block/template_shulker_box",
                list(kv("shulker", texture), kv("particle", particle)),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + shulkerBoxStr), pack);
        addParentTexModel("minecraft:block/template_shulker_box_bottom",
                list(kv("shulker", texture), kv("particle", particle)),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + shulkerBoxStr + "_bottom"), pack);
        addParentTexModel("minecraft:block/template_shulker_box_lid",
                list(kv("shulker", texture), kv("particle", particle)),
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + shulkerBoxStr + "_lid"), pack);
    }

    public static void addShulkerBoxBlockStates(@Nullable DyeColor color, EBEPack pack) {
        var shulkerBoxStr = color != null ? color.getName() + "_shulker_box" : "shulker_box";
        addBlockState(ResourceLocation.withDefaultNamespace(shulkerBoxStr),
                t -> {
                    var vars = new DelimitedAppender(",");
                    vars
                            .append(variant(t, "facing=up", MOD_ID + ":block/" + shulkerBoxStr))
                            .append(variantXY(t, "facing=down", MOD_ID + ":block/" + shulkerBoxStr, 180, 0));
                    for (Direction dir : EBEUtil.HORIZONTAL_DIRECTIONS) {
                        int rot = (int) dir.toYRot() + 180;
                        vars.append(variantXY(t, "facing=" + dir.getName(),
                                MOD_ID + ":block/" + shulkerBoxStr, 90, rot));
                    }
                    return vars.get();
                }, pack);
    }

    public static void resetBasePack() {
        BASE_PACK = new EBEPack(EBEUtil.id("base_resources"), EnhancedBlockEntities.TEMPLATE_LOADER);
    }

    public static void resetTopLevelPack() {
        TOP_LEVEL_PACK = new EBEPack(EBEUtil.id("top_level_resources"), EnhancedBlockEntities.TEMPLATE_LOADER);
    }

    public static EBEPack getBasePack() {
        return BASE_PACK;
    }

    public static EBEPack getTopLevelPack() {
        return TOP_LEVEL_PACK;
    }

    public static EBEPack getPackForCompat() {
        if (EnhancedBlockEntities.CONFIG.forceResourcePackCompat) {
            return getTopLevelPack();
        }
        return getBasePack();
    }

    public static void dumpModAssets(Path dest) throws IOException {
        var modContainer = ModList.get().getModContainerById(EnhancedBlockEntities.MOD_ID);
        var roots = modContainer.map(container -> List.of(container.getModInfo().getOwningFile().getFile().getFilePath())).orElse(List.of());

        for (var root : roots) {
            try (var sourceAssets = Files.walk(root.resolve("assets"))) {
                for (var asset : sourceAssets.collect(Collectors.toSet())) {
                    if (!Files.isDirectory(asset)) {
                        var out = dest.resolve(root.relativize(asset));
                        if (!Files.exists(out.getParent())) {
                            Files.createDirectories(out.getParent());
                        }
                        Files.copy(asset, out, Files.exists(out) ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
                    }
                }
            }
        }
    }

    public static void dumpAllPacks(Path dest) throws IOException {
        getBasePack().dump(dest);
        getTopLevelPack().dump(dest);
        dumpModAssets(dest);
    }

    private static class DelimitedAppender {
        private final StringBuilder builder = new StringBuilder();
        private final CharSequence delimiter;

        private DelimitedAppender(CharSequence delimiter) {
            this.delimiter = delimiter;
        }

        public DelimitedAppender append(CharSequence seq) {
            if (!this.builder.isEmpty()) {
                this.builder.append(this.delimiter);
            }
            this.builder.append(seq);
            return this;
        }

        public String get() {
            return this.builder.toString();
        }
    }
}