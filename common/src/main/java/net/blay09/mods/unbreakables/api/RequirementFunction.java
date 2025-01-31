package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.ResourceLocation;

public interface RequirementFunction<TRequirement extends BreakRequirement, TParameter> extends BreakModifierFunction<TRequirement, TParameter> {
    ResourceLocation getId();

    ResourceLocation getRequirementType();

    Class<TParameter> getParameterType();

    boolean isEnabled();
}

