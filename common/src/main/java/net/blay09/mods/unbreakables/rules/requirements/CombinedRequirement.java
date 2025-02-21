package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.rules.hint.CombinedHint;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.Optional;

public record CombinedRequirement(Collection<BreakRequirement> requirements) implements BreakRequirement {

    @Override
    public boolean canAfford(BreakContext context, Player player) {
        return requirements.stream().allMatch(requirement -> requirement.canAfford(context, player));
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
    public Optional<BreakHint<?>> hint(BreakContext context, Player player) {
        return Optional.of(new CombinedHint(requirements.stream()
                .map(it -> it.hint(context, player))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList()));
    }

    @Override
    public boolean isEmpty() {
        return requirements.stream().allMatch(BreakRequirement::isEmpty);
    }
}
