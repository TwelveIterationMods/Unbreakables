package net.blay09.mods.unbreakables;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.ConfiguredCondition;
import net.blay09.mods.unbreakables.rules.CombinedRequirement;
import net.blay09.mods.unbreakables.rules.ConfiguredRequirementModifier;
import net.blay09.mods.unbreakables.rules.NoRequirement;
import net.blay09.mods.unbreakables.rules.RuleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BreakContextImpl implements BreakContext {

    private final Map<ResourceLocation, BreakRequirement> requirements = new HashMap<>();
    private final BlockGetter blockGetter;
    private final BlockPos pos;
    private final BlockState state;
    private final Player player;

    public BreakContextImpl(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player) {
        this.blockGetter = blockGetter;
        this.pos = pos;
        this.state = state;
        this.player = player;
    }

    @SuppressWarnings("unchecked")
    public <T extends BreakRequirement, P> void apply(ConfiguredRequirementModifier<T, P> configuredModifier) {
        for (final var condition : configuredModifier.conditions()) {
            if (!matchesCondition(condition)) {
                return;
            }
        }

        final var requirement = configuredModifier.requirement();
        final var modifier = requirement.modifier();
        final var parameters = requirement.parameters();
        var existing = (T) requirements.get(modifier.getRequirementType());
        if (existing == null) {
            existing = RuleRegistry.<T>getRequirementType(modifier.getRequirementType()).createInstance();
        }
        requirements.put(modifier.getRequirementType(), modifier.apply(existing, this, parameters));
    }

    public float getContextValue(ResourceLocation id) {
        final var resolver = RuleRegistry.getVariableResolver(id);
        if (resolver != null) {
            return resolver.resolve(this);
        }

        return 0f;
    }

    public <P> boolean matchesCondition(ConfiguredCondition<P> configuredCondition) {
        return configuredCondition.resolver().matches(this, configuredCondition.parameters());
    }

    public BreakRequirement resolve() {
        if (requirements.isEmpty()) {
            return NoRequirement.INSTANCE;
        } else if (requirements.size() == 1) {
            return requirements.values().iterator().next();
        }
        return new CombinedRequirement(requirements.values());
    }

    @Override
    public BlockGetter getBlockGetter() {
        return blockGetter;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public BlockState getState() {
        return state;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
