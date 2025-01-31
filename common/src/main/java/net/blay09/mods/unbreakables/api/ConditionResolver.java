package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.ResourceLocation;

public interface ConditionResolver<P> {
    ResourceLocation getId();

    Class<P> getParameterType();

    boolean matches(BreakContext context, P parameters);
}
