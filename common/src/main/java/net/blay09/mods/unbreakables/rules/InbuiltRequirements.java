package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.rules.parameters.*;
import net.blay09.mods.unbreakables.rules.requirements.ExperienceLevelRequirementType;
import net.blay09.mods.unbreakables.rules.requirements.ExperiencePointsRequirementType;
import net.blay09.mods.unbreakables.rules.requirements.ItemRequirementType;
import net.blay09.mods.unbreakables.rules.requirements.RefuseRequirement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public class InbuiltRequirements {
    public static void register() {
        final var experiencePointRequirements = new ExperiencePointsRequirementType();
        final var experienceLevelRequirements = new ExperienceLevelRequirementType();
        final var itemRequirements = new ItemRequirementType();

        RuleRegistry.register(experiencePointRequirements);
        RuleRegistry.register(experienceLevelRequirements);
        RuleRegistry.register(itemRequirements);

        RuleRegistry.registerModifier("add_level_cost", experienceLevelRequirements, FloatParameter.class, (cost, context, parameters) -> {
            cost.setLevels((int) (cost.getLevels() + parameters.value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("multiply_level_cost", experienceLevelRequirements, FloatParameter.class, (cost, context, parameters) -> {
            cost.setLevels((int) (cost.getLevels() * parameters.value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("scaled_add_level_cost", experienceLevelRequirements, VariableScaledParameter.class, (cost, context, parameters) -> {
            final var sourceValue = context.getContextValue(parameters.id().value());
            cost.setLevels((int) (cost.getLevels() + sourceValue * parameters.scale().value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("scaled_multiply_level_cost", experienceLevelRequirements, VariableScaledParameter.class, (cost, context, parameters) -> {
            final var sourceValue = context.getContextValue(parameters.id().value());
            cost.setLevels((int) (cost.getLevels() * sourceValue * parameters.scale().value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("min_level_cost", experienceLevelRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setLevels(Math.max(cost.getLevels(), parameters.value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("max_level_cost", experienceLevelRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setLevels(Math.min(cost.getLevels(), parameters.value()));
            return cost;
        }, () -> true);

        RuleRegistry.registerModifier("add_xp_cost", experiencePointRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setPoints(cost.getPoints() + parameters.value());
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("multiply_xp_cost", experiencePointRequirements, FloatParameter.class, (cost, context, parameters) -> {
            cost.setPoints((int) (cost.getPoints() * parameters.value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("scaled_add_xp_cost", experiencePointRequirements, VariableScaledParameter.class, (cost, context, parameters) -> {
            final var sourceValue = context.getContextValue(parameters.id().value());
            cost.setPoints((int) (cost.getPoints() + sourceValue * parameters.scale().value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("scaled_multiply_level_cost", experiencePointRequirements, VariableScaledParameter.class, (cost, context, parameters) -> {
            final var sourceValue = context.getContextValue(parameters.id().value());
            cost.setPoints((int) (cost.getPoints() * sourceValue * parameters.scale().value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("min_xp_cost", experiencePointRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setPoints(Math.max(cost.getPoints(), parameters.value()));
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("max_xp_cost", experiencePointRequirements, IntParameter.class, (cost, context, parameters) -> {
            cost.setPoints(Math.min(cost.getPoints(), parameters.value()));
            return cost;
        }, () -> true);

        RuleRegistry.registerModifier("add_item_cost", itemRequirements, FloatCountedIdParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.id().value());
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) parameters.count().value());
            } else {
                cost.setCount((int) (cost.getCount() + parameters.count().value()));
            }
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("multiply_item_cost", itemRequirements, FloatCountedIdParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.id().value());
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) parameters.count().value());
            } else {
                cost.setCount((int) (cost.getCount() * parameters.count().value()));
            }
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("scaled_add_item_cost", itemRequirements, VariableScaledItemParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.item().value());
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) (context.getContextValue(parameters.variable().value()) * parameters.count().value()));
            } else {
                cost.setCount((int) (cost.getCount() + context.getContextValue(parameters.variable().value()) * parameters.count().value()));
            }
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("scaled_multiply_item_cost", itemRequirements, VariableScaledItemParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.item().value());
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) (context.getContextValue(parameters.variable().value()) * parameters.count().value()));
            } else {
                cost.setCount((int) (cost.getCount() * context.getContextValue(parameters.variable().value()) * parameters.count().value()));
            }
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("min_item_cost", itemRequirements, FloatCountedIdParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.id().value());
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) parameters.count().value());
            } else {
                cost.setCount(Math.max(cost.getCount(), (int) parameters.count().value()));
            }
            return cost;
        }, () -> true);
        RuleRegistry.registerModifier("max_item_cost", itemRequirements, FloatCountedIdParameter.class, (cost, context, parameters) -> {
            final var item = BuiltInRegistries.ITEM.get(parameters.id().value());
            if (cost.getItemStack().getItem() != item) {
                cost.setItemStack(new ItemStack(item));
                cost.setCount((int) parameters.count().value());
            } else {
                cost.setCount(Math.min(cost.getCount(), (int) parameters.count().value()));
            }
            return cost;
        }, () -> true);

        RuleRegistry.registerModifier("refuse", RuleRegistry.createDefaultType("refuse", RefuseRequirement.class), ComponentParameter.class, (cost, context, parameters) -> {
            cost.setMessage(parameters.value());
            return cost;
        }, () -> true);
    }
}
