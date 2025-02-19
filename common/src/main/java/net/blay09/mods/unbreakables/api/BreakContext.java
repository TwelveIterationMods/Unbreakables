package net.blay09.mods.unbreakables.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Function;

public interface BreakContext {
    <P> boolean matchesCondition(ConfiguredCondition<P> configuredCondition);

    float getContextValue(ResourceLocation id);

    Player getPlayer();

    BlockGetter getBlockGetter();

    BlockPos getPos();

    BlockState getState();

    boolean viaServer(Function<ServerLevel, Boolean> runner);
}
