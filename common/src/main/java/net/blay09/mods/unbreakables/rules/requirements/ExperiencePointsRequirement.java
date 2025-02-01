package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ExperiencePointsRequirement implements BreakRequirement {
    private int points;

    public ExperiencePointsRequirement(int points) {
        this.points = Math.max(0, points);
    }

    @Override
    public boolean canAfford(Player player) {
        final var xpForLevel = getCumulativeXpNeededForLevel(player.experienceLevel);
        final var xpForProgress = Math.floor(player.experienceProgress * getXpNeededForNextLevel(player.experienceLevel));
        final var totalXp = xpForLevel + xpForProgress;
        return totalXp >= points;
    }

    @Override
    public void consume(Player player) {
        player.giveExperiencePoints(-points);
    }

    @Override
    public void rollback(Player player) {
        player.giveExperiencePoints(points);
    }

    @Override
    public void appendHoverText(Player player, List<Component> tooltip) {
        if (points > 0) {
            tooltip.add(Component.translatable("chat.unbreakables.xp_requirement", points).withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public boolean isEmpty() {
        return points <= 0;
    }

    public static int calculateLevelCostFromExperiencePoints(int currentLevel, int xpLoss) {
        return currentLevel - calculateLevelMinusExperiencePoints(currentLevel, xpLoss);
    }

    private static int calculateLevelMinusExperiencePoints(int currentLevel, int xpLoss) {
        int currentCumulativeXp = getCumulativeXpNeededForLevel(currentLevel);

        int remainingXp = currentCumulativeXp - xpLoss;

        int newLevel = 0;
        int newCumulativeXp = 0;
        for (int level = 0; level <= currentLevel; level++) {
            newCumulativeXp += getXpNeededForNextLevel(level);
            if (remainingXp < newCumulativeXp) {
                newLevel = level;
                break;
            }
        }

        return newLevel;
    }

    private static int getXpNeededForNextLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }

    private static int getCumulativeXpNeededForLevel(int targetLevel) {
        int currentCumulativeXp = 0;
        for (int level = 0; level < targetLevel; level++) {
            currentCumulativeXp += getXpNeededForNextLevel(level);
        }
        return currentCumulativeXp;
    }

    public void setPoints(int value) {
        this.points = value;
    }

    public int getPoints() {
        return points;
    }
}
