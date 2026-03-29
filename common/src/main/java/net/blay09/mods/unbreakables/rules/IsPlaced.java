package net.blay09.mods.unbreakables.rules;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import net.blay09.mods.shogi.context.ShogiContext;
import net.blay09.mods.shogi.effect.ShogiEffect;
import net.blay09.mods.shogi.effect.failure.ShogiDeferred;
import net.blay09.mods.unbreakables.PlacedBlockTracker;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

import static net.blay09.mods.unbreakables.Unbreakables.id;

public class IsPlaced implements ShogiEffect<Boolean> {

    public static final Identifier IDENTIFIER = id("is_placed");
    public static final IsPlaced INSTANCE = new IsPlaced();
    public static final MapCodec<IsPlaced> MAP_CODEC = MapCodec.unit(INSTANCE);

    private IsPlaced() {
    }

    @Override
    public Either<Boolean, ?> apply(ShogiContext context) {
        final var level = context.requireLevel();
        final var pos = context.requireBlockPos();
        if (level instanceof ServerLevel serverLevel) {
            return Either.left(PlacedBlockTracker.get(serverLevel).isPlaced(pos));
        } else {
            return Either.right(ShogiDeferred.INSTANCE);
        }
    }

    @Override
    public Identifier identifier() {
        return IDENTIFIER;
    }
}
