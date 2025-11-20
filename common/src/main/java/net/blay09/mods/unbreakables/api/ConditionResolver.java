package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.Identifier;

public interface ConditionResolver<P> {
    Identifier getId();

    Class<P> getParameterType();

    boolean matches(BreakContext context, P parameters);
}
