package net.blay09.mods.unbreakables.client.hint;

import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BreakHintClientRegistry {
    private static final Map<ResourceLocation, BreakHintRenderer<?>> renderers = new HashMap<>();

    public static void register(ResourceLocation id, BreakHintRenderer<?> renderer) {
        renderers.put(id, renderer);
    }

    public static BreakHintRenderer<?> getRenderer(ResourceLocation id) {
        return renderers.get(id);
    }
}
