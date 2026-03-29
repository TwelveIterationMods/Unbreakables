package net.blay09.mods.unbreakables.mixin;

import net.blay09.mods.unbreakables.PlacedBlockTracker;
import net.blay09.mods.unbreakables.ModBlockTags;
import net.blay09.mods.unbreakables.UnbreakablesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {

    @Shadow
    @Final
    private Level level;

    @Shadow
    public abstract ChunkStatus getPersistedStatus();

    @Inject(method = "setBlockState(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"))
    private void trackPlacedBlocks(BlockPos pos, BlockState newState, @Block.UpdateFlags int flags, CallbackInfoReturnable<@Nullable BlockState> cir) {
        if (!(level instanceof ServerLevel serverLevel) || !getPersistedStatus().isOrAfter(ChunkStatus.FULL)) {
            return;
        }

        final var oldState = cir.getReturnValue();
        if (oldState == null) {
            return;
        }

        if (!unbreakables$shouldTrack(serverLevel, pos, newState)) {
            return;
        }

        final var isNewlyPlaced = oldState.isAir() || (!oldState.getFluidState().isEmpty() && newState.getFluidState().isEmpty());
        final var isSameBlock = oldState.getBlock() == newState.getBlock();
        if (isSameBlock || !isNewlyPlaced) {
            return;
        }

        final var tracker = PlacedBlockTracker.get(serverLevel);
        if (newState.isAir()) {
            tracker.clearPlaced(pos);
        } else if (oldState.isAir() || oldState.getBlock() != newState.getBlock()) {
            tracker.markPlaced(pos, oldState, newState);
        }
    }

    @Unique
    private static boolean unbreakables$shouldTrack(ServerLevel serverLevel, BlockPos pos, BlockState state) {
        if (state.is(ModBlockTags.DO_NOT_TRACK)) {
            return false;
        }

        if (!state.getFluidState().isEmpty()) {
            return false;
        }

        return switch (UnbreakablesConfig.getActive().placedBlockTracking) {
            case NONE -> false;
            case IN_STRUCTURE -> {
                final var structureManager = serverLevel.structureManager();
                final var structures = structureManager.getAllStructuresAt(pos);
                for (final var structure : structures.keySet()) {
                    final var structureStart = structureManager.getStructureAt(pos, structure);
                    if (structureManager.structureHasPieceAt(pos, structureStart)) {
                        yield true;
                    }
                }
                yield false;
            }
        };
    }
}
