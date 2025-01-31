package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public interface BreakContext {
    <P> boolean matchesCondition(ConfiguredCondition<P> configuredCondition);

    float getContextValue(ResourceLocation id);

    Player getPlayer();

    BlockState getState();
}
