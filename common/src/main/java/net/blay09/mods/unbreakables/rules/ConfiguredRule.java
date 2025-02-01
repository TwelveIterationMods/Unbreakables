package net.blay09.mods.unbreakables.rules;

import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.ConfiguredCondition;
import net.blay09.mods.unbreakables.rules.requirements.ConfiguredRequirement;

import java.util.List;

public record ConfiguredRule<T extends BreakRequirement, P>(ConfiguredRequirement<T, P> requirement, List<ConfiguredCondition<?>> conditions) {
}
