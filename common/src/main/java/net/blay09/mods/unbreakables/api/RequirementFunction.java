package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.Identifier;

public interface RequirementFunction<TRequirement extends BreakRequirement, TParameter> extends BreakModifierFunction<TRequirement, TParameter> {
    Identifier getId();

    Identifier getRequirementType();

    Class<TParameter> getParameterType();

    boolean isEnabled();
}

