package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.EventPhases;
import net.blay09.mods.balm.platform.event.callback.BlockCallback;
import net.blay09.mods.unbreakables.network.ModNetworking;
import net.blay09.mods.unbreakables.rules.UnbreakablesRules;
import net.blay09.mods.unbreakables.rules.hint.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unbreakables {

    public static final Logger logger = LoggerFactory.getLogger(Unbreakables.class);

    public static final String MOD_ID = "unbreakables";

    public static void initialize(BalmRegistrars registrars) {
        BreakHintRegistry.register(CombinedHint.ID, CombinedHint.CombinedBreakHintSerializer.INSTANCE);
        BreakHintRegistry.register(NoHint.ID, NoHint.NoHintSerializer.INSTANCE);
        BreakHintRegistry.register(MessageHint.ID, MessageHint.MessageHintSerializer.INSTANCE);
        BreakHintRegistry.register(CooldownHint.ID, CooldownHint.CooldownHintSerializer.INSTANCE);
        BreakHintRegistry.register(ExperienceLevelHint.ID, ExperienceLevelHint.ExperienceLevelHintSerializer.INSTANCE);
        BreakHintRegistry.register(ExperiencePointsHint.ID, ExperiencePointsHint.ExperiencePointsHintSerializer.INSTANCE);
        BreakHintRegistry.register(ItemHint.ID, ItemHint.ItemHintSerializer.INSTANCE);

        UnbreakablesConfig.initialize();
        UnbreakablesRules.initialize();

        ModNetworking.initialize(Balm.networking());

        // Disable dig speed for breakable blocks
        BlockCallback.DigSpeed.EVENT.register((blockGetter, pos, state, player, speed) -> {
            final var breakContext = BreakTracker.getOrCreateContext(blockGetter, pos, state, player,
                    context -> context.resolveSimulatedAndSync());
            return !breakContext.resolveSimulatedAndSync() ? 0f : speed;
        });

        // In case the break somehow goes through the dig speed, run as early as possible to cancel the block break
        BlockCallback.Break.Before.EVENT.register(EventPhases.HIGHEST, (level, pos, state, blockEntity, player) -> {
            if (player.getAbilities().instabuild) {
                return true;
            }

            final var breakContext = new BreakContextImpl(level, pos, state, player);
            if (!breakContext.resolveSimulatedAndSync()) {
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
            if (!breakContext.resolveImmediate()) {
                return false;
            }

            return true;
        });
    }

}
