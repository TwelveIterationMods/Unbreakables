package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.rules.parameters.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class InbuiltConditions {
    public static void register() {
        RuleRegistry.registerConditionResolver("is_block",
                TaggableIdParameter.class,
                (context, parameters) -> parameters.isTag() ? context.getState()
                        .is(TagKey.create(Registries.BLOCK, parameters.value())) : BuiltInRegistries.BLOCK.getKey(context.getState().getBlock())
                        .equals(parameters.value()));

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

        RuleRegistry.registerConditionResolver("players_nearby",
                BiFloatParameter.class,
                (context, parameters) -> pickLevel(context).map(level -> level.getEntities(context.getPlayer(),
                        AABB.ofSize(context.getPos().getCenter(), parameters.first().value(), parameters.first().value(), parameters.first().value()),
                        EntitySelector.NO_SPECTATORS).size() > parameters.second().value()).orElse(false));

        RuleRegistry.registerConditionResolver("mobs_nearby",
                BiFloatParameter.class,
                (context, parameters) -> pickLevel(context).map(level -> level.getEntities(context.getPlayer(),
                        AABB.ofSize(context.getPos().getCenter(), parameters.first().value(), parameters.first().value(), parameters.first().value()),
                        it -> it instanceof Mob).size() > parameters.second().value()).orElse(false));

        RuleRegistry.registerConditionResolver("animals_nearby",
                BiFloatParameter.class,
                (context, parameters) -> pickLevel(context).map(level -> level.getEntities(context.getPlayer(),
                        AABB.ofSize(context.getPos().getCenter(), parameters.first().value(), parameters.first().value(), parameters.first().value()),
                        it -> it instanceof Animal).size() > parameters.second().value()).orElse(false));

        RuleRegistry.registerConditionResolver("entity_nearby",
                EntityNearbyParameter.class,
                (context, parameters) -> pickLevel(context).map(level -> level.getEntities(context.getPlayer(), AABB.ofSize(context.getPos().getCenter(),
                                        parameters.distance().value(),
                                        parameters.distance().value(),
                                        parameters.distance().value()),
                                it -> parameters.entity().isTag() ? it.getType()
                                        .is(TagKey.create(Registries.ENTITY_TYPE, parameters.entity().value())) : BuiltInRegistries.ENTITY_TYPE.getKey(it.getType())
                                        .equals(parameters.entity().value()))
                        .size() > parameters.minimum().value()).orElse(false));

        RuleRegistry.registerConditionResolver("is_above_y",
                FloatParameter.class,
                (context, parameters) -> {
                    final var pos = context.getPos();
                    return pos.getY() > parameters.value();
                });

        RuleRegistry.registerConditionResolver("is_below_y",
                FloatParameter.class,
                (context, parameters) -> {
                    final var pos = context.getPos();
                    return pos.getY() < parameters.value();
                });

        RuleRegistry.registerConditionResolver("is_within",
                BoundsParameter.class,
                (context, parameters) -> {
                    final var pos = context.getPos();
                    return pos.getX() >= parameters.minX().value() && pos.getX() <= parameters.maxX().value() &&
                            pos.getY() >= parameters.minY().value() && pos.getY() <= parameters.maxY().value() &&
                            pos.getZ() >= parameters.minZ().value() && pos.getZ() <= parameters.maxZ().value();
                });

        RuleRegistry.registerConditionResolver("is_at",
                PositionParameter.class,
                (context, parameters) -> {
                    final var pos = context.getPos();
                    return pos.getX() == parameters.x().value() && pos.getY() == parameters.y().value() && pos.getZ() == parameters.z().value();
                });

        RuleRegistry.registerConditionResolver("is_near",
                IsNearParameter.class,
                (context, parameters) -> {
                    final var pos = context.getPos();
                    final var rulePos = new BlockPos(parameters.x().value(), parameters.y().value(), parameters.z().value());
                    final var maxDist = parameters.distance().value();
                    final var dist = pos.distSqr(rulePos);
                    return dist <= maxDist * maxDist;
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
