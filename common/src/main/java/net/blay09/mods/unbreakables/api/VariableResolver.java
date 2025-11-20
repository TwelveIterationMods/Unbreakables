package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.Identifier;

public interface VariableResolver {
    Identifier getId();
    float resolve(BreakContext context);
}
