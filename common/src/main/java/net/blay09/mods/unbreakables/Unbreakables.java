package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.BreakBlockEvent;
import net.blay09.mods.balm.api.event.EventPriority;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.unbreakables.api.UnbreakablesAPI;
import net.blay09.mods.unbreakables.event.NewDigSpeedEvent;
import net.blay09.mods.unbreakables.network.ModNetworking;
import net.blay09.mods.unbreakables.network.UnbreakableRulesMessage;
import net.blay09.mods.unbreakables.rules.InbuiltConditions;
import net.blay09.mods.unbreakables.rules.InbuiltParameters;
import net.blay09.mods.unbreakables.rules.InbuiltRequirements;
import net.blay09.mods.unbreakables.rules.hint.*;
import net.blay09.mods.unbreakables.rulesets.RulesetLoader;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unbreakables {

    public static final Logger logger = LoggerFactory.getLogger(Unbreakables.class);

    public static final String MOD_ID = "unbreakables";

    public static void initialize() {
        InbuiltParameters.register();
        InbuiltConditions.register();
        InbuiltRequirements.register();

        UnbreakablesAPI.registerHintSerializer(CombinedHint.ID, CombinedHint.CombinedBreakHintSerializer.INSTANCE);
        UnbreakablesAPI.registerHintSerializer(NoHint.ID, NoHint.NoHintSerializer.INSTANCE);
        UnbreakablesAPI.registerHintSerializer(MessageHint.ID, MessageHint.MessageHintSerializer.INSTANCE);
        UnbreakablesAPI.registerHintSerializer(CooldownHint.ID, CooldownHint.CooldownHintSerializer.INSTANCE);
        UnbreakablesAPI.registerHintSerializer(ExperienceLevelHint.ID, ExperienceLevelHint.ExperienceLevelHintSerializer.INSTANCE);
        UnbreakablesAPI.registerHintSerializer(ExperiencePointsHint.ID, ExperiencePointsHint.ExperiencePointsHintSerializer.INSTANCE);
        UnbreakablesAPI.registerHintSerializer(ItemHint.ID, ItemHint.ItemHintSerializer.INSTANCE);

        UnbreakablesConfig.initialize();

        ModNetworking.initialize(Balm.getNetworking());

        Balm.addServerReloadListener(ResourceLocation.fromNamespaceAndPath(MOD_ID, "json_rulesets"), new RulesetLoader());

        // Sync rules to clients so they can properly predict if a block can be broken
        Balm.getEvents()
                .onEvent(PlayerLoginEvent.class,
                        event -> Balm.getNetworking().sendTo(event.getPlayer(), new UnbreakableRulesMessage(RulesetLoader.getRules())));

        // Disable dig speed for breakable blocks
        Balm.getEvents().onEvent(NewDigSpeedEvent.class, (event) -> {
            final var breakContext = BreakTracker.getOrCreateContext(event.getBlockGetter(),
                    event.getPos(),
                    event.getState(),
                    event.getPlayer(),
                    (context) -> RulesetLoader.getLoadedRules().forEach(it -> ((BreakContextImpl) context).apply(it)));
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(breakContext, event.getPlayer())) {
                event.setSpeedOverride(0f);
            }
        });

        // In case the break somehow goes through the dig speed, run as early as possible to cancel the block break
        Balm.getEvents().onEvent(BreakBlockEvent.class, (event) -> {
            if (event.getPlayer().getAbilities().instabuild) {
                return;
            }

            final var breakContext = new BreakContextImpl(event.getLevel(), event.getPos(), event.getState(), event.getPlayer());
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(breakContext, event.getPlayer())) {
                event.setCanceled(true);
            }
        }, EventPriority.Highest);

        // If the block break is not cancelled, consume requirements
        Balm.getEvents().onEvent(BreakBlockEvent.class, (event) -> {
            if (event.getPlayer().getAbilities().instabuild) {
                return;
            }

            final var breakContext = new BreakContextImpl(event.getLevel(), event.getPos(), event.getState(), event.getPlayer());
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(breakContext, event.getPlayer())) {
                event.setCanceled(true);
            } else {
                requirement.consume(event.getPlayer());
            }
        }, EventPriority.Lowest);
    }

}
