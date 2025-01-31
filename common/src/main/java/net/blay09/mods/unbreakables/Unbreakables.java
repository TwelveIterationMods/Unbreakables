package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.BreakBlockEvent;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.balm.api.event.EventPriority;
import net.blay09.mods.unbreakables.rules.BreakContextImpl;
import net.blay09.mods.unbreakables.rules.RuleRegistry;
import net.blay09.mods.unbreakables.rulesets.RulesetLoader;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unbreakables {

    public static final Logger logger = LoggerFactory.getLogger(Unbreakables.class);

    public static final String MOD_ID = "unbreakables";

    public static void initialize() {
        RuleRegistry.registerDefaults();
        UnbreakablesConfig.initialize();

        Balm.addServerReloadListener(new ResourceLocation(MOD_ID, "json_rulesets"), new RulesetLoader());

        Balm.getEvents().onEvent(DigSpeedEvent.class, (event) -> {
            final var breakContext = new BreakContextImpl(event.getPlayer(), event.getState());
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(event.getPlayer())) {
                event.setSpeedOverride(0f);
            }
        });

        Balm.getEvents().onEvent(BreakBlockEvent.class, (event) -> {
            final var breakContext = new BreakContextImpl(event.getPlayer(), event.getState());
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(event.getPlayer())) {
                event.setCanceled(true);
            }
        }, EventPriority.Highest);

        Balm.getEvents().onEvent(BreakBlockEvent.class, (event) -> {
            final var breakContext = new BreakContextImpl(event.getPlayer(), event.getState());
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(event.getPlayer())) {
                event.setCanceled(true);
            } else {
                requirement.consume(event.getPlayer());
            }
        }, EventPriority.Lowest);
    }

}
