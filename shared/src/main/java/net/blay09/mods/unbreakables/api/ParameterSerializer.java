package net.blay09.mods.unbreakables.api;

public interface ParameterSerializer<T> {
    Class<T> getType();
    T deserialize(String value);
}
