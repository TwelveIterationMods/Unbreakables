package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.rules.parameters.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.Optional;

public class InbuiltConditions {
    public static void register() {
        RuleRegistry.registerConditionResolver("is_block",
                IdParameter.class,
                (context, parameters) -> BuiltInRegistries.BLOCK.getKey(context.getState().getBlock()).equals(parameters.value()));

        RuleRegistry.registerConditionResolver("is_tag",
                IdParameter.class,
                (context, parameters) -> context.getState().is(TagKey.create(Registries.BLOCK, parameters.value())));

        RuleRegistry.registerConditionResolver("is_state",
                PropertyParameter.class,
                (context, parameters) -> {
                    for (final var property : context.getState().getProperties()) {
                        if (property.getName().equals(parameters.property().value())) {
                            final var value = context.getState().getValue(property);
                            final var stringValue = value instanceof StringRepresentable rep ? rep.getSerializedName() : value.toString();
                            if (stringValue.equals(parameters.value().value())) {
                                return true;
                            }
                        }
                    }
                    return false;
                });

        RuleRegistry.registerConditionResolver("is_in_dimension",
                IdParameter.class,
                (context, parameters) -> pickLevel(context).map(it -> it.dimension().location().equals(parameters.value())).orElse(false));

        RuleRegistry.registerConditionResolver("is_in_biome",
                TaggableIdParameter.class,
                (context, parameters) -> pickLevelAccessor(context).map(it -> parameters.isTag() ? it.getBiome(context.getPos())
                        .is(TagKey.create(Registries.BIOME, parameters.value())) : it.getBiome(context.getPos()).is(parameters.value())).orElse(false));

        RuleRegistry.registerConditionResolver("has_effect",
                FloatCountedIdParameter.class,
                (context, parameters) -> {
                    final var effect = BuiltInRegistries.MOB_EFFECT.get(parameters.id().value());
                    if (effect != null) {
                        final var effectInstance = context.getPlayer().getEffect(effect);
                        if (effectInstance != null) {
                            final var amplifier = effectInstance.getAmplifier();
                            return amplifier >= parameters.count().value();
                        }
                        return false;
                    }
                    return false;
                });

        RuleRegistry.registerConditionResolver("is_tool",
                IdParameter.class,
                (context, parameters) -> context.getPlayer().getMainHandItem().getItem() == BuiltInRegistries.ITEM.get(parameters.value()));

        RuleRegistry.registerConditionResolver("is_enchanted",
                FloatCountedIdParameter.class,
                (context, parameters) -> {
                    final var item = context.getPlayer().getMainHandItem();
                    final var enchantment = BuiltInRegistries.ENCHANTMENT.get(parameters.id().value());
                    if (enchantment != null) {
                        final var level = EnchantmentHelper.getItemEnchantmentLevel(enchantment, item);
                        return level >= parameters.count().value();
                    }
                    return false;
                });
    }

    private static Optional<LevelAccessor> pickLevelAccessor(BreakContext context) {
        if (context.getBlockGetter() instanceof LevelAccessor level) {
            return Optional.of(level);
        }
        return Optional.ofNullable(context.getPlayer()).map(Entity::level);
    }

    private static Optional<Level> pickLevel(BreakContext context) {
        if (context.getBlockGetter() instanceof Level level) {
            return Optional.of(level);
        }
        return Optional.ofNullable(context.getPlayer()).map(Entity::level);
    }
}
