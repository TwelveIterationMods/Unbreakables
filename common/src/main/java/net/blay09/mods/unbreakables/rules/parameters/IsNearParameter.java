package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.FloatParameter;
import net.blay09.mods.unbreakables.api.parameter.IntParameter;

public record IsNearParameter(IntParameter x, IntParameter y, IntParameter z, FloatParameter distance) {
}
