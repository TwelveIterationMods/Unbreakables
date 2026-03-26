package net.blay09.mods.unbreakables.client.hint;

import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BreakHintClientRegistry {
    private static final Map<Identifier, BreakHintRenderer<?>> renderers = new HashMap<>();

    public static void register(Identifier id, BreakHintRenderer<?> renderer) {
        renderers.put(id, renderer);
    }

    @Nullable
    public static BreakHintRenderer<?> getRenderer(Identifier id) {
        return renderers.get(id);
    }
}
