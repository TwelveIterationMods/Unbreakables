package net.blay09.mods.unbreakables.api;

import net.minecraft.resources.Identifier;

public interface RequirementType<T extends BreakRequirement> {
    Identifier getId();
    T createInstance();
}
