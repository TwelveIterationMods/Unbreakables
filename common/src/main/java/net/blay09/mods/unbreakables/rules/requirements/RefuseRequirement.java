package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.blay09.mods.unbreakables.rules.hint.MessageHint;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class RefuseRequirement implements BreakRequirement {

    private Component message = Component.empty();

    public RefuseRequirement() {
    }

    public RefuseRequirement(Component message) {
        this.message = message;
    }

    @Override
    public boolean canAfford(BreakContext context, Player player) {
        return false;
    }

    @Override
    public void consume(Player player) {
    }

    @Override
    public void rollback(Player player) {
    }

    @Override
    public Optional<BreakHint<?>> hint(BreakContext context, Player player) {
        return Optional.of(new MessageHint(message));
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public Component getMessage() {
        return message;
    }
}
