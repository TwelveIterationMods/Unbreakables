package net.blay09.mods.unbreakables;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.blay09.mods.unbreakables.Unbreakables.id;

public class PlacedBlockTracker extends SavedData {

    private static final Codec<PlacedBlockTracker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.listOf().fieldOf("placed_blocks").forGetter(PlacedBlockTracker::getPlacedBlockList)
    ).apply(instance, PlacedBlockTracker::new));

    @SuppressWarnings("DataFlowIssue")
    private static final SavedDataType<PlacedBlockTracker> TYPE = new SavedDataType<>(
            id("placed_blocks"),
            () -> new PlacedBlockTracker(List.of()),
            CODEC,
            null
    );

    private final Set<Long> placedBlocks = new HashSet<>();

    public PlacedBlockTracker(List<Long> placedBlocks) {
        this.placedBlocks.addAll(placedBlocks);
    }

    public static PlacedBlockTracker get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public boolean isPlaced(BlockPos pos) {
        return placedBlocks.contains(pos.asLong());
    }

    public void markPlaced(BlockPos pos, BlockState state, BlockState newState) {
        if (placedBlocks.add(pos.asLong())) {
            setDirty();
        }
    }

    public void clearPlaced(BlockPos pos) {
        if (placedBlocks.remove(pos.asLong())) {
            setDirty();
        }
    }

    private List<Long> getPlacedBlockList() {
        return List.copyOf(placedBlocks);
    }
}
