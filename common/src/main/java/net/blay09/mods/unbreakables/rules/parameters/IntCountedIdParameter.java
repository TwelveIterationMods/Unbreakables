package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.IdParameter;
import net.blay09.mods.unbreakables.api.parameter.IntParameter;

public record IntCountedIdParameter(IdParameter id, IntParameter level) {
}
