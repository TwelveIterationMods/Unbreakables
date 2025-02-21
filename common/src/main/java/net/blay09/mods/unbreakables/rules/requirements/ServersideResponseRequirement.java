package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public record ServersideResponseRequirement(BreakHint<?> hint, boolean breakable) implements BreakRequirement {
    @Override
    public boolean canAfford(BreakContext context, Player player) {
        return breakable;
    }

    @Override
    public void consume(Player player) {
    }

    @Override
    public void rollback(Player player) {
    }

    @Override
    public Optional<BreakHint<?>> hint(BreakContext context, Player player) {
        return Optional.of(hint);
    }
}
