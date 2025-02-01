package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.rules.RuleRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;

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
                (context, parameters) -> context.getPlayer().level().dimension().location().equals(parameters.value()));
    }
}
