package net.blay09.mods.unbreakables.rulesets;

import com.google.gson.Gson;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.UnbreakablesConfig;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class JsonRulesetLoader implements ResourceManagerReloadListener {

    private static final Gson gson = new Gson();
    private static final FileToIdConverter COMPAT_JSONS = FileToIdConverter.json("unbreakables");

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        for (final var entry : COMPAT_JSONS.listMatchingResources(resourceManager).entrySet()) {
            try (final var reader = entry.getValue().openAsReader()) {
                load(gson.fromJson(reader, JsonRulesetData.class));
            } catch (Exception e) {
                Unbreakables.logger.error("Parsing error loading Unbreakables rulesets at {}", entry.getKey(), e);
            }
        }
    }

    private static boolean isRulesetEnabled(String id) {
        return UnbreakablesConfig.getActive().rulesets.contains(id);
    }

    private static void load(JsonRulesetData data) {
        final var id = data.getId();
        if (isRulesetEnabled(id)) {
            return;
        }
    }

}
