package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ExperienceLevelRequirement implements BreakRequirement {
    private int levels;

    public ExperienceLevelRequirement(int levels) {
        this.levels = Math.max(0, levels);
    }

    @Override
    public boolean canAfford(Player player) {
        return player.experienceLevel >= levels;
    }

    @Override
    public void consume(Player player) {
        player.giveExperienceLevels(-levels);
    }

    @Override
    public void rollback(Player player) {
        player.giveExperienceLevels(levels);
    }

    @Override
    public void appendHoverText(Player player, List<Component> tooltip) {
        if (levels > 0) {
            tooltip.add(Component.translatable("chat.unbreakables.level_requirement", levels).withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public boolean isEmpty() {
        return levels <= 0;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getLevels() {
        return levels;
    }
}
