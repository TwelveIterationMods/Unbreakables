package net.blay09.mods.unbreakables;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class BreakTracker {

    private static final Map<UUID, BreakContext> contexts = new HashMap<>();

    public static void startBreak(Player player) {
        contexts.remove(getKeyForPlayer(player));
    }

    public static void stopBreak(Player player) {
        contexts.remove(getKeyForPlayer(player));
    }

    private static UUID getKeyForPlayer(Player player) {
        return player.getGameProfile().getId();
    }

    public static BreakContext getOrCreateContext(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player, Consumer<BreakContext> initializer) {
        return contexts.computeIfAbsent(getKeyForPlayer(player), (key) -> {
            final var breakContext = new BreakContextImpl(blockGetter, pos, state, player);
            initializer.accept(breakContext);
            return breakContext;
        });
    }

    public static Optional<BreakContext> getContext(Player player, BlockPos pos) {
        final var cacheKey = getKeyForPlayer(player);
        final var breakContext = contexts.get(cacheKey);
        if (breakContext != null && breakContext.getPos().equals(pos)) {
            return Optional.of(breakContext);
        }
        contexts.remove(cacheKey);
        return Optional.empty();
    }
}
