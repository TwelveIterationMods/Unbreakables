package net.blay09.mods.unbreakables.rules.parameters;

import net.blay09.mods.unbreakables.api.parameter.FloatParameter;
import net.blay09.mods.unbreakables.api.parameter.IdParameter;
import net.blay09.mods.unbreakables.api.parameter.UnbreakablesIdParameter;

public record VariableScaledFloatCountedIdParameter(UnbreakablesIdParameter variable, IdParameter item, FloatParameter count) {
}
