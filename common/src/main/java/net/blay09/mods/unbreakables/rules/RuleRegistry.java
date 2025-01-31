package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;

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

    public record NoParameter() {
        public static final NoParameter INSTANCE = new NoParameter();
    }

    public record IntParameter(int value) {
    }

    public record FloatParameter(float value) {
    }

    public record StringParameter(String value) {
    }

    public record IdParameter(ResourceLocation value) {
    }

    public record UnbreakablesIdParameter(ResourceLocation value) {
    }

    public record ComponentParameter(Component value) {
    }

    public record VariableScaledParameter(UnbreakablesIdParameter id, FloatParameter scale) {
    }

    public record CooldownParameter(UnbreakablesIdParameter id, FloatParameter seconds) {
    }

    public record VariableScaledCooldownParameter(UnbreakablesIdParameter variable, UnbreakablesIdParameter cooldown, FloatParameter seconds) {
    }

    public record ItemParameter(IdParameter item, FloatParameter count) {
    }

    public record VariableScaledItemParameter(UnbreakablesIdParameter variable, IdParameter item, FloatParameter count) {
    }

    public record PropertyParameter(StringParameter property, StringParameter value) {
    }

    public static void registerDefaults() {
        final var experiencePointRequirements = new ExperiencePointsRequirementType();
        final var experienceLevelRequirements = new ExperienceLevelRequirementType();
        final var itemRequirements = new ItemRequirementType();

        register(experiencePointRequirements);
        register(experienceLevelRequirements);
        register(itemRequirements);

        registerConditionResolver("is_block",
                IdParameter.class,
                (context, parameters) -> BuiltInRegistries.BLOCK.getKey(context.getState().getBlock()).equals(parameters.value));

        registerConditionResolver("is_tag",
                IdParameter.class,
                (context, parameters) -> context.getState().is(TagKey.create(Registries.BLOCK, parameters.value)));

        registerConditionResolver("is_state",
                PropertyParameter.class,
                (context, parameters) -> {
                    for (final var property : context.getState().getProperties()) {
                        if (property.getName().equals(parameters.property.value)) {
                            final var value = context.getState().getValue(property);
                            if (value.toString().equals(parameters.value.value)) {
                                return true;
                            }
                        }
                    }
                    return false;
                });

        registerConditionResolver("is_in_dimension",
                IdParameter.class,
                (context, parameters) -> context.getPlayer().level().dimension().location().equals(parameters.value));

        registerModifier("add_level_cost", experienceLevelRequirements, FloatParameter.class, (cost, context, parameters) -> {
            cost.setLevels((int) (cost.getLevels() + parameters.value));
            return cost;
        }, () -> true);
        registerModifier("multiply_level_cost", experienceLevelRequirements, FloatParameter.class, (cost, context, parameters) -> {
            cost.setLevels((int) (cost.getLevels() * parameters.value));
            return cost;
        }, () -> true);
        registerModifier("scaled_add_level_cost", experienceLevelRequirements, VariableScaledParameter.class, (cost, context, parameters) -> {
            final var sourceValue = context.getContextValue(parameters.id.value);
            cost.setLevels((int) (cost.getLevels() + sourceValue * parameters.scale.value));
            return cost;
        }, () -> true);
        registerModifier("scaled_multiply_level_cost", experienceLevelRequirements, VariableScaledParameter.class, (cost, context, parameters) -> {
            final var sourceValue = context.getContextValue(parameters.id.value);
            cost.setLevels((int) (cost.getLevels() * sourceValue * parameters.scale.value));
            return cost;
        }, () -> true);
        registerModifier("min_level_cost", experienceLevelRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setLevels(Math.max(cost.getLevels(), parameters.value));
            return cost;
        }, () -> true);
        registerModifier("max_level_cost", experienceLevelRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setLevels(Math.min(cost.getLevels(), parameters.value));
            return cost;
        }, () -> true);

        registerModifier("add_xp_cost", experiencePointRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setPoints(cost.getPoints() + parameters.value);
            return cost;
        }, () -> true);
        registerModifier("multiply_xp_cost", experiencePointRequirements, FloatParameter.class, (cost, context, parameters) -> {
            cost.setPoints((int) (cost.getPoints() * parameters.value));
            return cost;
        }, () -> true);
        registerModifier("scaled_add_xp_cost", experiencePointRequirements, VariableScaledParameter.class, (cost, context, parameters) -> {
            final var sourceValue = context.getContextValue(parameters.id.value);
            cost.setPoints((int) (cost.getPoints() + sourceValue * parameters.scale.value));
            return cost;
        }, () -> true);
        registerModifier("scaled_multiply_level_cost", experiencePointRequirements, VariableScaledParameter.class, (cost, context, parameters) -> {
            final var sourceValue = context.getContextValue(parameters.id.value);
            cost.setPoints((int) (cost.getPoints() * sourceValue * parameters.scale.value));
            return cost;
        }, () -> true);
        registerModifier("min_xp_cost", experiencePointRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setPoints(Math.max(cost.getPoints(), parameters.value));
            return cost;
        }, () -> true);
        registerModifier("max_xp_cost", experiencePointRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setPoints(Math.min(cost.getPoints(), parameters.value));
            return cost;
        }, () -> true);

        registerModifier("add_item_cost", itemRequirements, ItemParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.item.value);
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) parameters.count.value);
            } else {
                cost.setCount((int) (cost.getCount() + parameters.count.value));
            }
            return cost;
        }, () -> true);
        registerModifier("multiply_item_cost", itemRequirements, ItemParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.item.value);
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) parameters.count.value);
            } else {
                cost.setCount((int) (cost.getCount() * parameters.count.value));
            }
            return cost;
        }, () -> true);
        registerModifier("scaled_add_item_cost", itemRequirements, VariableScaledItemParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.item.value);
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) (context.getContextValue(parameters.variable.value) * parameters.count.value));
            } else {
                cost.setCount((int) (cost.getCount() + context.getContextValue(parameters.variable.value) * parameters.count.value));
            }
            return cost;
        }, () -> true);
        registerModifier("scaled_multiply_item_cost", itemRequirements, VariableScaledItemParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.item.value);
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) (context.getContextValue(parameters.variable.value) * parameters.count.value));
            } else {
                cost.setCount((int) (cost.getCount() * context.getContextValue(parameters.variable.value) * parameters.count.value));
            }
            return cost;
        }, () -> true);
        registerModifier("min_item_cost", itemRequirements, ItemParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.item.value);
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) parameters.count.value);
            } else {
                cost.setCount(Math.max(cost.getCount(), (int) parameters.count.value));
            }
            return cost;
        }, () -> true);
        registerModifier("max_item_cost", itemRequirements, ItemParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.item.value);
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) parameters.count.value);
            } else {
                cost.setCount(Math.min(cost.getCount(), (int) parameters.count.value));
            }
            return cost;
        }, () -> true);

        registerModifier("refuse", createDefaultType("refuse", RefuseRequirement.class), ComponentParameter.class, (cost, context, parameters) -> {
            cost.setMessage(parameters.value);
            return cost;
        }, () -> true);

        registerSerializer(NoParameter.class, it -> NoParameter.INSTANCE);
        registerSerializer(IntParameter.class, it -> new IntParameter(Integer.parseInt(it)));
        registerSerializer(FloatParameter.class, it -> new FloatParameter(Float.parseFloat(it)));
        registerSerializer(StringParameter.class, StringParameter::new);
        registerSerializer(IdParameter.class, it -> new IdParameter(ResourceLocation.tryParse(it)));
        registerSerializer(UnbreakablesIdParameter.class, it -> new UnbreakablesIdParameter(RequirementModifierParser.unbreakablesResourceLocation(it)));
        registerSerializer(ComponentParameter.class,
                it -> new ComponentParameter(it.startsWith("$") ? Component.translatable(it.substring(1)) : Component.literal(it)));
        registerDefaultSerializer(VariableScaledParameter.class);
        registerDefaultSerializer(CooldownParameter.class);
        registerDefaultSerializer(VariableScaledCooldownParameter.class);
        registerDefaultSerializer(ItemParameter.class);
        registerDefaultSerializer(VariableScaledItemParameter.class);
        registerDefaultSerializer(PropertyParameter.class);
    }

    private static <T extends BreakRequirement> RequirementType<T> createDefaultType(String name, Class<T> requirementClass) {
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
        registerSerializer(type, it -> RequirementModifierParser.deserializeParameterList(type, it));
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

    private static <T extends BreakRequirement, P> void registerModifier(String name, RequirementType<T> requirementType, Class<P> parameterType, BreakModifierFunction<T, P> function, Supplier<Boolean> predicate) {
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
