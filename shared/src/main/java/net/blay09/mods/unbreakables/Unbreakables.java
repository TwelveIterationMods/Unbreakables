package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.unbreakables.rules.BreakContextImpl;
import net.blay09.mods.unbreakables.rules.RequirementModifierParser;
import net.blay09.mods.unbreakables.rules.RuleRegistry;
import net.blay09.mods.unbreakables.rulesets.JsonRulesetLoader;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unbreakables {

    public static final Logger logger = LoggerFactory.getLogger(Unbreakables.class);

    public static final String MOD_ID = "unbreakables";

    public static void initialize() {
        RuleRegistry.registerDefaults();
        UnbreakablesConfig.initialize();

        Balm.addServerReloadListener(new ResourceLocation(MOD_ID, "json_rulesets"), new JsonRulesetLoader());

        Balm.getEvents().onEvent(DigSpeedEvent.class, (event) -> {
            final var breakContext = new BreakContextImpl(event.getPlayer(), event.getState());
            final var configuredModifiers = UnbreakablesConfig.getActive().rules;
            for (final var modifier : configuredModifiers) {
                if (modifier.isBlank()) {
                    continue;
                }

                RequirementModifierParser.parse(modifier)
                        .filter(configuredModifier -> configuredModifier.requirement().modifier().isEnabled())
                        .ifPresent(breakContext::apply);
            }

            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(event.getPlayer())) {
                event.setSpeedOverride(0f);
            }
        });
    }

}
