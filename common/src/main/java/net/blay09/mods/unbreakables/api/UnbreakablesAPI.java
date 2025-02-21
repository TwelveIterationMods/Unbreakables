package net.blay09.mods.unbreakables.api;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class UnbreakablesAPI {

    private static final InternalMethods internalMethods;

    static {
        try {
            internalMethods = (InternalMethods) Class.forName("net.blay09.mods.unbreakables.InternalMethodsImpl").getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void registerParameterSerializer(Class<T> type, Function<String, T> deserializer) {
        internalMethods.registerParameterSerializer(type, deserializer);
    }

    public static <T> void registerDefaultParameterSerializer(Class<T> type) {
        internalMethods.registerDefaultParameterSerializer(type);
    }

    public static <P> void registerCondition(String name, Class<P> parameterType, BiFunction<BreakContext, P, Boolean> resolver) {
        internalMethods.registerCondition(name, parameterType, resolver);
    }

    public static <T extends BreakRequirement, P> void registerModifier(String name, RequirementType<T> requirementType, Class<P> parameterType, BreakModifierFunction<T, P> function, Supplier<Boolean> predicate) {
        internalMethods.registerModifier(name, requirementType, parameterType, function, predicate);
    }
}
