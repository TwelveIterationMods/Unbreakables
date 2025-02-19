package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.FloatParameter;
import net.blay09.mods.unbreakables.api.parameter.UnbreakablesIdParameter;

public record CooldownParameter(UnbreakablesIdParameter id, FloatParameter seconds) {
}
