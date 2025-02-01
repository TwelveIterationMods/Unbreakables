package net.blay09.mods.unbreakables.rulesets;

import com.google.gson.Gson;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.UnbreakablesConfig;
import net.blay09.mods.unbreakables.network.UnbreakableRulesMessage;
import net.blay09.mods.unbreakables.rules.ConfiguredRequirementModifier;
import net.blay09.mods.unbreakables.rules.RequirementModifierParser;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RulesetLoader implements ResourceManagerReloadListener {

    private static final Gson gson = new Gson();
    private static final FileToIdConverter COMPAT_JSONS = FileToIdConverter.json("unbreakables");

    private static final List<String> rules = new ArrayList<>();
    private static final Set<String> knownRulesets = new HashSet<>();
    private static final List<ConfiguredRequirementModifier<?, ?>> loadedRules = new ArrayList<>();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        reset();
        load(UnbreakablesConfig.getActive().rules);

        final var enabledRulesets = UnbreakablesConfig.getActive().rulesets;
        for (final var entry : COMPAT_JSONS.listMatchingResources(resourceManager).entrySet()) {
            try (final var reader = entry.getValue().openAsReader()) {
                final var rulesetId = entry.getKey().getNamespace() + ":" + entry.getKey().getPath().replace("unbreakables/", "").replace(".json", "");
                knownRulesets.add(rulesetId);
                if (enabledRulesets.contains(rulesetId)) {
                    load(gson.fromJson(reader, JsonRulesetData.class).getRules());
                }
            } catch (Exception e) {
                Unbreakables.logger.error("Parsing error loading Unbreakables rulesets at {}", entry.getKey(), e);
            }
        }

        for (final var enabledRuleset : enabledRulesets) {
            if (!knownRulesets.contains(enabledRuleset)) {
                Unbreakables.logger.warn("Unknown Unbreakables ruleset: {}", enabledRuleset);
            }
        }

        Unbreakables.logger.info("{} unbreakable rules loaded", loadedRules.size());

        final var server = Balm.getHooks().getServer();
        if (server != null) {
            Balm.getNetworking().sendToAll(server, new UnbreakableRulesMessage(RulesetLoader.getRules()));
        }
    }

    public static void reset() {
        rules.clear();
        loadedRules.clear();
        knownRulesets.clear();
    }

    public static void load(List<String> rules) {
        RulesetLoader.rules.addAll(rules);
        for (final var rule : rules) {
            if (rule.isBlank()) {
                continue;
            }

            Unbreakables.logger.info("Loading unbreakable rule {}", rule);
            RequirementModifierParser.parse(rule)
                    .filter(configuredModifier -> configuredModifier.requirement().modifier().isEnabled())
                    .ifPresent(loadedRules::add);
        }
    }

    public static List<String> getRules() {
        return rules;
    }

    public static List<ConfiguredRequirementModifier<?, ?>> getLoadedRules() {
        return loadedRules;
    }

}
