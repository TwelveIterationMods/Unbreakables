package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface InternalMethods {
    <P> void registerCondition(String name, Class<P> parameterType, BiFunction<BreakContext,P, Boolean> resolver);

    <T> void registerParameterSerializer(Class<T> type, Function<String, T> deserializer);

    <T> void registerDefaultParameterSerializer(Class<T> type);

    <T extends BreakRequirement, P> void registerModifier(String name, RequirementType<T> requirementType, Class<P> parameterType, BreakModifierFunction<T,P> function, Supplier<Boolean> predicate);

    <T> void registerHintSerializer(Identifier id, BreakHint.Serializer<T> serializer);
}
