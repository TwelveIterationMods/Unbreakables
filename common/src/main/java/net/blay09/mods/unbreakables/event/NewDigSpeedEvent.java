package net.blay09.mods.unbreakables.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class NewDigSpeedEvent extends BalmEvent {
    private final BlockGetter blockGetter;
    private final BlockPos pos;
    private final BlockState state;
    private final float speed;
    private final Player player;
    private Float speedOverride;

    public NewDigSpeedEvent(BlockGetter blockGetter, BlockPos pos, BlockState state, float speed, Player player) {
        this.blockGetter = blockGetter;
        this.pos = pos;
        this.state = state;
        this.speed = speed;
        this.player = player;
    }

    public BlockGetter getBlockGetter() {
        return blockGetter;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    public float getSpeed() {
        return speed;
    }

    public Player getPlayer() {
        return player;
    }

    public Float getSpeedOverride() {
        return speedOverride;
    }

    public void setSpeedOverride(Float speedOverride) {
        this.speedOverride = speedOverride;
    }
}
