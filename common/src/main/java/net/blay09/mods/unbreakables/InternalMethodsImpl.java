package net.blay09.mods.unbreakables;

import net.blay09.mods.unbreakables.api.*;
import net.blay09.mods.unbreakables.rules.RuleRegistry;
import net.blay09.mods.unbreakables.rules.hint.BreakHintRegistry;
import net.minecraft.resources.ResourceLocation;

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
    public <T> void registerHintSerializer(ResourceLocation id, BreakHint.Serializer<T> serializer) {
        BreakHintRegistry.register(id, serializer);
    }
}
