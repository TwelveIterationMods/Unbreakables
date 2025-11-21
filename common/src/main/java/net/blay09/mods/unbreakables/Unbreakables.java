package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.EventPhases;
import net.blay09.mods.balm.platform.event.callback.BlockCallback;
import net.blay09.mods.balm.platform.event.callback.ServerPlayerCallback;
import net.blay09.mods.unbreakables.api.UnbreakablesAPI;
import net.blay09.mods.unbreakables.network.ModNetworking;
import net.blay09.mods.unbreakables.network.UnbreakableRulesMessage;
import net.blay09.mods.unbreakables.rules.InbuiltConditions;
import net.blay09.mods.unbreakables.rules.InbuiltParameters;
import net.blay09.mods.unbreakables.rules.InbuiltRequirements;
import net.blay09.mods.unbreakables.rules.hint.*;
import net.blay09.mods.unbreakables.rulesets.RulesetLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unbreakables {

    public static final Logger logger = LoggerFactory.getLogger(Unbreakables.class);

    public static final String MOD_ID = "unbreakables";

    public static void initialize(BalmRegistrars registrars) {
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

        ModNetworking.initialize(Balm.networking());

        registrars.resourceReloadListeners(registrar
                -> registrar.register("json_rulesets", new RulesetLoader()));

        // Sync rules to clients so they can properly predict if a block can be broken
        ServerPlayerCallback.Join.EVENT.register(player -> Balm.networking().sendTo(player, new UnbreakableRulesMessage(RulesetLoader.getRules())));

        // Disable dig speed for breakable blocks
        BlockCallback.DigSpeed.EVENT.register((blockGetter, pos, state, player, speed) -> {
            final var breakContext = BreakTracker.getOrCreateContext(blockGetter, pos, state, player,
                    (context) -> RulesetLoader.getLoadedRules().forEach(it -> ((BreakContextImpl) context).apply(it)));
            final var requirement = breakContext.resolve();
            return !requirement.canAfford(breakContext, player) ? 0f : speed;
        });

        // In case the break somehow goes through the dig speed, run as early as possible to cancel the block break
        BlockCallback.Break.Before.EVENT.register(EventPhases.HIGHEST, (level, pos, state, blockEntity, player) -> {
            if (player.getAbilities().instabuild) {
                return true;
            }

            final var breakContext = new BreakContextImpl(level, pos, state, player);
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(breakContext, player)) {
                return false;
            }
            return true;
        });

        // If the block break is not cancelled, consume requirements
        BlockCallback.Break.Before.EVENT.register(EventPhases.LOWEST, (level, pos, state, blockEntity, player) -> {
            if (player.getAbilities().instabuild) {
                return true;
            }

            final var breakContext = new BreakContextImpl(level, pos, state, player);
            RulesetLoader.getLoadedRules().forEach(breakContext::apply);
            final var requirement = breakContext.resolve();
            if (!requirement.canAfford(breakContext, player)) {
                return false;
            } else {
                requirement.consume(player);
            }

            return true;
        });
    }

}
