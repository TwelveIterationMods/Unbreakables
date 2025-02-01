package net.blay09.mods.unbreakables.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface BreakContext {
    <P> boolean matchesCondition(ConfiguredCondition<P> configuredCondition);

    float getContextValue(ResourceLocation id);

    Player getPlayer();

    BlockGetter getBlockGetter();

    BlockPos getPos();

    BlockState getState();
}
