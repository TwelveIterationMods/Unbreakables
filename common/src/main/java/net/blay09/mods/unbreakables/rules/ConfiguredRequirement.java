package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.RequirementFunction;

public record ConfiguredRequirement<T extends BreakRequirement, P>(RequirementFunction<T, P> modifier, P parameters) {
}
