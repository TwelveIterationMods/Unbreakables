package net.blay09.mods.unbreakables.api;

public interface BreakModifierFunction<TRequirement, TParameter> {
    TRequirement apply(TRequirement requirement, BreakContext context, TParameter parameters);
}
