package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.minecraft.world.entity.player.Player;

public class NoRequirement implements BreakRequirement {
    public static final BreakRequirement INSTANCE = new NoRequirement();

    @Override
    public boolean canAfford(BreakContext context, Player player) {
        return true;
    }

    @Override
    public void consume(Player player) {
    }

    @Override
    public void rollback(Player player) {
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
