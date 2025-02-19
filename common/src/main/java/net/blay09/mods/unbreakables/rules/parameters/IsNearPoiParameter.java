package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.FloatParameter;
import net.blay09.mods.unbreakables.api.parameter.IntParameter;
import net.blay09.mods.unbreakables.api.parameter.TaggableIdParameter;

public record IsNearPoiParameter(TaggableIdParameter poi, FloatParameter distance) {
}
