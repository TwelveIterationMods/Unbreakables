package net.blay09.mods.unbreakables;

import com.mojang.datafixers.util.Either;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.shogi.context.MutableShogiContext;
import net.blay09.mods.shogi.context.executor.EffectExecutor;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.blay09.mods.unbreakables.network.ClientboundUnbreakableStatusPacket;
import net.blay09.mods.unbreakables.rules.UnbreakablesRules;
import net.blay09.mods.unbreakables.rules.hint.NoHint;
import net.blay09.mods.unbreakables.rules.hint.ShogiHintFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.lang.ref.WeakReference;

public class BreakContextImpl {

    private final BlockGetter blockGetter;
    private final BlockPos pos;
    private final BlockState state;
    private final WeakReference<@Nullable Player> player;

    private boolean resolved;
    private boolean breakable = true;
    private @Nullable BreakHint<?> hint = NoHint.INSTANCE;
    private @Nullable Boolean serverBreakable;
    private @Nullable BreakHint<?> serverHint;

    public BreakContextImpl(BlockGetter blockGetter, BlockPos pos, BlockState state, @Nullable Player player) {
        this.blockGetter = blockGetter;
        this.pos = pos;
        this.state = state;
        this.player = new WeakReference<>(player);
    }

    public boolean resolveSimulatedAndSync() {
        if (!resolved) {
            final var result = evaluate(EffectExecutor.simulated());
            breakable = result.breakable;
            hint = result.hint;
            resolved = true;
            final var player = this.player.get();
            if (player instanceof ServerPlayer serverPlayer) {
                Balm.networking().sendTo(serverPlayer, new ClientboundUnbreakableStatusPacket(pos, result.hint, result.breakable));
            }
        }
        return getBreakable();
    }

    public boolean resolveImmediate() {
        final var result = evaluate(EffectExecutor.immediate());
        breakable = result.breakable;
        hint = result.hint;
        resolved = true;
        return getBreakable();
    }

    private EvaluationResult evaluate(EffectExecutor executor) {
        final var player = this.player.get();
        if (player == null) {
            return new EvaluationResult(true, NoHint.INSTANCE);
        }

        final var context = MutableShogiContext.create(executor)
                .withEntity(player)
                .withBlockPos(pos)
                .withBlockState(state)
                .withItemStack(player.getMainHandItem());
        if (blockGetter instanceof Level level) {
            context.withLevel(level);
        }

        final var breakRequirements = UnbreakablesRules.breakRequirements.get(context);
        final var breakable = breakRequirements.left().isPresent();
        final var payload = Either.unwrap(breakRequirements);
        if (breakRequirements.right().isPresent() && payload instanceof Throwable t) {
            Unbreakables.logger.error("Unhandled exception while evaluating unbreakable rules", t);
        }
        return new EvaluationResult(breakable, ShogiHintFactory.fromPayload(payload));
    }

    public void applyServerResponse(BreakHint<?> hint, boolean breakable) {
        serverHint = hint;
        serverBreakable = breakable;
    }

    public boolean getBreakable() {
        return serverBreakable != null ? serverBreakable : breakable;
    }

    public BreakHint<?> getHint() {
        return serverHint != null ? serverHint : (hint != null ? hint : NoHint.INSTANCE);
    }

    public BlockPos getPos() {
        return pos;
    }

    private record EvaluationResult(boolean breakable, BreakHint<?> hint) {
    }
}
