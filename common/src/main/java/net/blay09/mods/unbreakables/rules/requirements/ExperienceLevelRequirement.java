package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.blay09.mods.unbreakables.rules.hint.ExperienceLevelHint;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class ExperienceLevelRequirement implements BreakRequirement {
    private int levels;

    public ExperienceLevelRequirement(int levels) {
        this.levels = Math.max(0, levels);
    }

    @Override
    public boolean canAfford(BreakContext context, Player player) {
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
    public boolean isEmpty() {
        return levels <= 0;
    }

    @Override
    public Optional<BreakHint<?>> hint(BreakContext context, Player player) {
        return Optional.of(new ExperienceLevelHint(levels));
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getLevels() {
        return levels;
    }
}
