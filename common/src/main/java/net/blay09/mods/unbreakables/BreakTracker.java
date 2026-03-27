package net.blay09.mods.unbreakables;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class BreakTracker {

    private static final Map<Player, BreakContextImpl> contexts = Collections.synchronizedMap(new WeakHashMap<>());

    public static void startBreak(Player player) {
        contexts.remove(player);
    }

    public static void stopBreak(Player player) {
        contexts.remove(player);
    }

    public static BreakContextImpl getOrCreateContext(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player, Consumer<BreakContextImpl> initializer) {
        return contexts.computeIfAbsent(player, (key) -> {
            final var breakContext = new BreakContextImpl(blockGetter, pos, state, player);
            initializer.accept(breakContext);
            return breakContext;
        });
    }

    public static Optional<BreakContextImpl> getContext(Player player) {
        return getContext(player, null);
    }

    public static Optional<BreakContextImpl> getContext(Player player, @Nullable BlockPos pos) {
        final var breakContext = contexts.get(player);
        if (breakContext != null && (pos == null || breakContext.getPos().equals(pos))) {
            return Optional.of(breakContext);
        }
        contexts.remove(player);
        return Optional.empty();
    }
}
