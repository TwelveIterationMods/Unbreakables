package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.FloatParameter;
import net.blay09.mods.unbreakables.api.parameter.TaggableIdParameter;

public record EntityNearbyParameter(TaggableIdParameter entity, FloatParameter distance, FloatParameter minimum) {
}
