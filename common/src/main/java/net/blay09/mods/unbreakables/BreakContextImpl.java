package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.ConfiguredCondition;
import net.blay09.mods.unbreakables.network.ClientboundUnbreakableStatusPacket;
import net.blay09.mods.unbreakables.rules.ConfiguredRule;
import net.blay09.mods.unbreakables.rules.RuleRegistry;
import net.blay09.mods.unbreakables.rules.hint.NoHint;
import net.blay09.mods.unbreakables.rules.requirements.ClientsideAssumedRequirement;
import net.blay09.mods.unbreakables.rules.requirements.CombinedRequirement;
import net.blay09.mods.unbreakables.rules.requirements.NoRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BreakContextImpl implements BreakContext {

    private final Map<ResourceLocation, BreakRequirement> requirements = new HashMap<>();
    private final BlockGetter blockGetter;
    private final BlockPos pos;
    private final BlockState state;
    private final WeakReference<Player> player;

    private boolean hasServersideConditions;
    private BreakRequirement resolvedRequirement;

    public BreakContextImpl(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player) {
        this.blockGetter = blockGetter;
        this.pos = pos;
        this.state = state;
        this.player = new WeakReference<>(player);
    }

    @SuppressWarnings("unchecked")
    public <T extends BreakRequirement, P> void apply(ConfiguredRule<T, P> configuredModifier) {
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
        if (resolvedRequirement != null) {
            return resolvedRequirement;
        }

        BreakRequirement result;
        if (requirements.isEmpty()) {
            result = NoRequirement.INSTANCE;
        } else if (requirements.size() == 1) {
            result = requirements.values().iterator().next();
        } else {
            result = new CombinedRequirement(requirements.values());
        }
        resolvedRequirement = result;

        final var player = this.player.get();
        if (player != null) {
            boolean breakable = resolvedRequirement.canAfford(this, player);
            if (hasServersideConditions && player instanceof ServerPlayer) {
                Balm.getNetworking().sendTo(player, new ClientboundUnbreakableStatusPacket(pos, result.hint(this, player).orElse(NoHint.INSTANCE), breakable));
            }
        }

        return result;
    }

    public void resolve(BreakRequirement resolvedRequirement) {
        this.resolvedRequirement = resolvedRequirement;
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
    public boolean viaServer(Function<ServerLevel, Boolean> runner) {
        hasServersideConditions = true;
        if (blockGetter instanceof ServerLevel serverLevel) {
            return runner.apply(serverLevel);
        }
        // Server-side conditions are always false on the client.
        // However, we mark the client to not simulate and instead start breaking until the server corrects us.
        resolvedRequirement = ClientsideAssumedRequirement.INSTANCE;
        return false;
    }

    @Override
    public Player getPlayer() {
        return player.get();
    }
}
