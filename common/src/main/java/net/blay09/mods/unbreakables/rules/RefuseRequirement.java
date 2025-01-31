package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class RefuseRequirement implements BreakRequirement {

    private Component message = Component.empty();

    public RefuseRequirement() {
    }

    public RefuseRequirement(Component message) {
        this.message = message;
    }

    @Override
    public boolean canAfford(Player player) {
        return false;
    }

    @Override
    public void consume(Player player) {
    }

    @Override
    public void rollback(Player player) {
    }

    @Override
    public void appendHoverText(Player player, List<Component> tooltip) {
        tooltip.add(message.copy().withStyle(ChatFormatting.RED));
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public Component getMessage() {
        return message;
    }
}
