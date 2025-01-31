package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.ResourceLocation;

public interface RequirementType<T extends BreakRequirement> {
    ResourceLocation getId();
    T createInstance();
}
