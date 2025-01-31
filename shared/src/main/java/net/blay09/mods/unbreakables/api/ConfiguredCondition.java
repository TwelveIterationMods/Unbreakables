package net.blay09.mods.unbreakables.api;

public record ConfiguredCondition<P>(ConditionResolver<P> resolver, P parameters) {
}
