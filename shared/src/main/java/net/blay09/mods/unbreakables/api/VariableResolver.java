package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.ResourceLocation;

public interface VariableResolver {
    ResourceLocation getId();
    float resolve(BreakContext context);
}
