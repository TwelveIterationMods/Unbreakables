package net.blay09.mods.unbreakables;

import net.blay09.mods.unbreakables.api.*;
import net.blay09.mods.unbreakables.rules.RuleRegistry;
import net.blay09.mods.unbreakables.rulesets.RulesetLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class InternalMethodsImpl implements InternalMethods {
    @Override
    public <P> void registerCondition(String name, Class<P> parameterType, BiFunction<BreakContext, P, Boolean> resolver) {
        RuleRegistry.registerConditionResolver(name, parameterType, resolver);
    }

    @Override
    public <T> void registerParameterSerializer(Class<T> type, Function<String, T> deserializer) {
        RuleRegistry.registerSerializer(type, deserializer);
    }

    @Override
    public <T> void registerDefaultParameterSerializer(Class<T> type) {
        RuleRegistry.registerDefaultSerializer(type);
    }

    @Override
    public <T extends BreakRequirement, P> void registerModifier(String name, RequirementType<T> requirementType, Class<P> parameterType, BreakModifierFunction<T, P> function, Supplier<Boolean> predicate) {
        RuleRegistry.registerModifier(name, requirementType, parameterType, function, predicate);
    }

    @Override
    public BreakRequirement resolveRequirements(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player) {
        final var breakContext = new BreakContextImpl(blockGetter, pos, state, player);
        RulesetLoader.getLoadedRules().forEach(breakContext::apply);
        return breakContext.resolve();
    }

    @Override
    public boolean testRequirements(BlockGetter blockGetter, BlockPos pos, BlockState state, Player player) {
        return resolveRequirements(blockGetter, pos, state, player).canAfford(player);
    }
}
