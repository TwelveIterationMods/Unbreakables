package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.*;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class RuleRegistry {

    private static final Map<ResourceLocation, RequirementType<?>> requirementTypes = new HashMap<>();
    private static final Map<ResourceLocation, RequirementFunction<?, ?>> requirementFunctions = new HashMap<>();
    private static final Map<Class<?>, ParameterSerializer<?>> parameterSerializers = new HashMap<>();
    private static final Map<ResourceLocation, VariableResolver> variableResolvers = new HashMap<>();
    private static final Map<ResourceLocation, ConditionResolver<?>> conditionResolvers = new HashMap<>();

    public static <T extends BreakRequirement> RequirementType<T> createDefaultType(String name, Class<T> requirementClass) {
        final var requirementType = new RequirementType<T>() {
            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(Unbreakables.MOD_ID, name);
            }

            @Override
            public T createInstance() {
                try {
                    return requirementClass.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        register(requirementType);
        return requirementType;
    }

    public static void register(RequirementType<?> requirementType) {
        requirementTypes.put(requirementType.getId(), requirementType);
    }

    public static void register(RequirementFunction<?, ?> requirementFunction) {
        requirementFunctions.put(requirementFunction.getId(), requirementFunction);
    }

    public static void register(ParameterSerializer<?> parameterSerializer) {
        parameterSerializers.put(parameterSerializer.getType(), parameterSerializer);
    }

    public static void register(VariableResolver variableResolver) {
        variableResolvers.put(variableResolver.getId(), variableResolver);
    }

    public static void register(ConditionResolver<?> conditionResolver) {
        conditionResolvers.put(conditionResolver.getId(), conditionResolver);
    }

    public static void registerVariableResolver(String name, Function<BreakContext, Float> resolver) {
        register(new VariableResolver() {
            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(Unbreakables.MOD_ID, name);
            }

            @Override
            public float resolve(BreakContext context) {
                return resolver.apply(context);
            }
        });
    }

    public static <P> void registerConditionResolver(String name, Class<P> parameterType, BiFunction<BreakContext, P, Boolean> resolver) {
        register(new ConditionResolver<P>() {
            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(Unbreakables.MOD_ID, name);
            }

            @Override
            public Class<P> getParameterType() {
                return parameterType;
            }

            @Override
            public boolean matches(BreakContext context, P parameters) {
                return resolver.apply(context, parameters);
            }
        });

        final var index = name.indexOf("is_");
        final var notName = index != -1 ? name.substring(0, index + 3) + "not_" + name.substring(index + 3) : "not_" + name;
        register(new ConditionResolver<P>() {
            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(Unbreakables.MOD_ID, notName);
            }

            @Override
            public Class<P> getParameterType() {
                return parameterType;
            }

            @Override
            public boolean matches(BreakContext context, P parameters) {
                return !resolver.apply(context, parameters);
            }
        });
    }

    public static <T> void registerDefaultSerializer(Class<T> type) {
        registerSerializer(type, it -> RuleParser.deserializeParameterList(type, it));
    }

    public static <T> void registerSerializer(Class<T> type, Function<String, T> deserializer) {
        register(new ParameterSerializer<T>() {
            @Override
            public Class<T> getType() {
                return type;
            }

            @Override
            public T deserialize(String value) {
                return deserializer.apply(value);
            }
        });
    }

    public static <T extends BreakRequirement, P> void registerModifier(String name, RequirementType<T> requirementType, Class<P> parameterType, BreakModifierFunction<T, P> function, Supplier<Boolean> predicate) {
        register(new RequirementFunction<T, P>() {
            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(Unbreakables.MOD_ID, name);
            }

            @Override
            public ResourceLocation getRequirementType() {
                return requirementType.getId();
            }

            @Override
            public Class<P> getParameterType() {
                return parameterType;
            }

            @Override
            public T apply(T requirement, BreakContext context, P parameters) {
                return function.apply(requirement, context, parameters);
            }

            @Override
            public boolean isEnabled() {
                return predicate.get();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends BreakRequirement> RequirementType<T> getRequirementType(ResourceLocation id) {
        return (RequirementType<T>) requirementTypes.get(id);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BreakRequirement, P> RequirementFunction<T, P> getRequirementFunction(ResourceLocation id) {
        return (RequirementFunction<T, P>) requirementFunctions.get(id);
    }

    public static VariableResolver getVariableResolver(ResourceLocation id) {
        return variableResolvers.get(id);
    }

    public static ConditionResolver<?> getConditionResolver(ResourceLocation id) {
        return conditionResolvers.get(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> ParameterSerializer<T> getParameterSerializer(Class<T> type) {
        return (ParameterSerializer<T>) parameterSerializers.get(type);
    }
}
