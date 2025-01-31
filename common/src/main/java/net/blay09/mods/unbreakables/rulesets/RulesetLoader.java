package net.blay09.mods.unbreakables.rulesets;

import com.google.gson.Gson;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.UnbreakablesConfig;
import net.blay09.mods.unbreakables.rules.ConfiguredRequirementModifier;
import net.blay09.mods.unbreakables.rules.RequirementModifierParser;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.ArrayList;
import java.util.List;

public class RulesetLoader implements ResourceManagerReloadListener {

    private static final Gson gson = new Gson();
    private static final FileToIdConverter COMPAT_JSONS = FileToIdConverter.json("unbreakables");

    private static final List<ConfiguredRequirementModifier<?, ?>> loadedRules = new ArrayList<>();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        loadedRules.clear();
        load(UnbreakablesConfig.getActive().rules);

        for (final var entry : COMPAT_JSONS.listMatchingResources(resourceManager).entrySet()) {
            try (final var reader = entry.getValue().openAsReader()) {
                if (isRulesetEnabled(entry.getKey().toString())) {
                    load(gson.fromJson(reader, JsonRulesetData.class).getRules());
                }
            } catch (Exception e) {
                Unbreakables.logger.error("Parsing error loading Unbreakables rulesets at {}", entry.getKey(), e);
            }
        }
    }

    private static boolean isRulesetEnabled(String id) {
        return UnbreakablesConfig.getActive().rulesets.contains(id);
    }

    private static void load(List<String> rules) {
        for (final var rule : rules) {
            if (rule.isBlank()) {
                continue;
            }

            RequirementModifierParser.parse(rule)
                    .filter(configuredModifier -> configuredModifier.requirement().modifier().isEnabled())
                    .ifPresent(loadedRules::add);
        }
    }

    public static List<ConfiguredRequirementModifier<?, ?>> getLoadedRules() {
        return loadedRules;
    }

}
