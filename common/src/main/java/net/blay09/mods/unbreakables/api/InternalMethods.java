package net.blay09.mods.unbreakables.api;

import net.blay09.mods.unbreakables.BreakContextImpl;
import net.blay09.mods.unbreakables.rulesets.RulesetLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface InternalMethods {
    <P> void registerCondition(String name, Class<P> parameterType, BiFunction<BreakContext,P, Boolean> resolver);

    <T> void registerParameterSerializer(Class<T> type, Function<String, T> deserializer);

    <T> void registerDefaultParameterSerializer(Class<T> type);

    <T extends BreakRequirement, P> void registerModifier(String name, RequirementType<T> requirementType, Class<P> parameterType, BreakModifierFunction<T,P> function, Supplier<Boolean> predicate);


    BreakRequirement resolveRequirements(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player);

    boolean testRequirements(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player);
}
