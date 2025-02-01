package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;

public class CombinedRequirement implements BreakRequirement {

    private final Collection<BreakRequirement> requirements;

    public CombinedRequirement(Collection<BreakRequirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    public boolean canAfford(Player player) {
        return requirements.stream().allMatch(requirement -> requirement.canAfford(player));
    }

    @Override
    public void consume(Player player) {
        requirements.forEach(requirement -> requirement.consume(player));
    }

    @Override
    public void rollback(Player player) {
        requirements.forEach(requirement -> requirement.rollback(player));
    }

    @Override
    public void appendHoverText(Player player, List<Component> tooltip) {
        requirements.forEach(requirement -> requirement.appendHoverText(player, tooltip));
    }

    @Override
    public boolean isEmpty() {
        return requirements.stream().allMatch(BreakRequirement::isEmpty);
    }

    public Collection<BreakRequirement> getRequirements() {
        return requirements;
    }
}
