package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.BreakBlockEvent;
import net.blay09.mods.balm.api.event.EventPriority;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.unbreakables.event.NewDigSpeedEvent;
import net.blay09.mods.unbreakables.network.ModNetworking;
import net.blay09.mods.unbreakables.network.UnbreakableRulesMessage;
import net.blay09.mods.unbreakables.rules.InbuiltConditions;
import net.blay09.mods.unbreakables.rules.InbuiltParameters;
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
        InbuiltParameters.register();
        InbuiltConditions.register();
        UnbreakablesConfig.initialize();

        ModNetworking.initialize(Balm.getNetworking());

        Balm.addServerReloadListener(ResourceLocation.fromNamespaceAndPath(MOD_ID, "json_rulesets"), new RulesetLoader());

        Balm.getEvents()
                .onEvent(PlayerLoginEvent.class,
                        event -> Balm.getNetworking().sendTo(event.getPlayer(), new UnbreakableRulesMessage(RulesetLoader.getRules())));

        Balm.getEvents().onEvent(NewDigSpeedEvent.class, (event) -> {
            final var breakContext = new BreakContextImpl(event.getBlockGetter(), event.getPos(), event.getState(), event.getPlayer());
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(event.getPlayer())) {
                event.setSpeedOverride(0f);
            }
        });

        Balm.getEvents().onEvent(BreakBlockEvent.class, (event) -> {
            if (event.getPlayer().getAbilities().instabuild) {
                return;
            }

            final var breakContext = new BreakContextImpl(event.getLevel(), event.getPos(), event.getState(), event.getPlayer());
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(event.getPlayer())) {
                event.setCanceled(true);
            }
        }, EventPriority.Highest);

        Balm.getEvents().onEvent(BreakBlockEvent.class, (event) -> {
            if (event.getPlayer().getAbilities().instabuild) {
                return;
            }

            final var breakContext = new BreakContextImpl(event.getLevel(), event.getPos(), event.getState(), event.getPlayer());
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
