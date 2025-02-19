package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.FloatParameter;
import net.blay09.mods.unbreakables.api.parameter.IdParameter;

public record FloatCountedIdParameter(IdParameter id, FloatParameter count) {
}
