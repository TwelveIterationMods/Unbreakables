package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.FloatParameter;
import net.blay09.mods.unbreakables.api.parameter.UnbreakablesIdParameter;

public record VariableScaledCooldownParameter(UnbreakablesIdParameter variable, UnbreakablesIdParameter cooldown, FloatParameter seconds) {
}
