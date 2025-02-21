package net.blay09.mods.unbreakables.api;

import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public interface BreakRequirement {
    boolean canAfford(BreakContext context, Player player);

    void consume(Player player);

    void rollback(Player player);

    default Optional<BreakHint<?>> hint(BreakContext context, Player player) {
        return Optional.empty();
    }

    default boolean isEmpty() {
        return false;
    }
}
