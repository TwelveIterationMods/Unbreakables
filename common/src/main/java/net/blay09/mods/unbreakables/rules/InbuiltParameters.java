package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.rules.parameters.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class InbuiltParameters {
    public static void register() {
        RuleRegistry.registerSerializer(NoParameter.class, it -> NoParameter.INSTANCE);
        RuleRegistry.registerSerializer(IntParameter.class, it -> new IntParameter(Integer.parseInt(it)));
        RuleRegistry.registerSerializer(FloatParameter.class, it -> new FloatParameter(Float.parseFloat(it)));
        RuleRegistry.registerSerializer(StringParameter.class, StringParameter::new);
        RuleRegistry.registerSerializer(IdParameter.class, it -> new IdParameter(ResourceLocation.tryParse(it)));
        RuleRegistry.registerSerializer(TaggableIdParameter.class,
                it -> it.startsWith("#") ? new TaggableIdParameter(ResourceLocation.tryParse(it.substring(1)),
                        true) : new TaggableIdParameter(ResourceLocation.tryParse(it), false));
        RuleRegistry.registerSerializer(UnbreakablesIdParameter.class, it -> new UnbreakablesIdParameter(RuleParser.unbreakablesResourceLocation(it)));
        RuleRegistry.registerSerializer(ComponentParameter.class,
                it -> new ComponentParameter(it.startsWith("$") ? Component.translatable(it.substring(1)) : Component.literal(it)));
        RuleRegistry.registerDefaultSerializer(VariableScaledParameter.class);
        RuleRegistry.registerDefaultSerializer(CooldownParameter.class);
        RuleRegistry.registerDefaultSerializer(VariableScaledCooldownParameter.class);
        RuleRegistry.registerDefaultSerializer(FloatCountedIdParameter.class);
        RuleRegistry.registerDefaultSerializer(BiFloatParameter.class);
        RuleRegistry.registerDefaultSerializer(VariableScaledFloatCountedIdParameter.class);
        RuleRegistry.registerDefaultSerializer(PropertyParameter.class);
        RuleRegistry.registerDefaultSerializer(IntCountedIdParameter.class);
        RuleRegistry.registerDefaultSerializer(EntityNearbyParameter.class);
        RuleRegistry.registerDefaultSerializer(PositionParameter.class);
        RuleRegistry.registerDefaultSerializer(BoundsParameter.class);
    }
}
