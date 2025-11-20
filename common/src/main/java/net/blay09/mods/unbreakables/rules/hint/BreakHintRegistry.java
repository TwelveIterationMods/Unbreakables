package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class BreakHintRegistry {
    private static final Map<Identifier, BreakHint.Serializer<?>> serializers = new HashMap<>();

    public static void register(Identifier id, BreakHint.Serializer<?> serializer) {
        serializers.put(id, serializer);
    }

    @SuppressWarnings("unchecked")
    public static BreakHint.Serializer<? extends BreakHint<?>> getSerializer(Identifier id) {
        return (BreakHint.Serializer<? extends BreakHint<?>>) serializers.get(id);
    }
}
