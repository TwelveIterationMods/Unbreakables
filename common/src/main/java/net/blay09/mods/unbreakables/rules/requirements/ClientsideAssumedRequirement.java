package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public record ClientsideAssumedRequirement() implements BreakRequirement {

    public static final ClientsideAssumedRequirement INSTANCE = new ClientsideAssumedRequirement();

    @Override
    public boolean canAfford(Player player) {
        return true;
    }

    @Override
    public void consume(Player player) {
    }

    @Override
    public void rollback(Player player) {
    }

    @Override
    public void appendHoverText(Player player, List<Component> tooltip) {
    }
}
