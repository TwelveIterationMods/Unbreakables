package net.blay09.mods.unbreakables.client;

import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.blay09.mods.unbreakables.api.client.InternalClientMethods;
import net.blay09.mods.unbreakables.client.hint.BreakHintClientRegistry;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.resources.Identifier;

public class InternalClientMethodsImpl implements InternalClientMethods {
    @Override
    public <T extends BreakHint<T>> void registerHintRenderer(Identifier id, BreakHintRenderer<T> renderer) {
        BreakHintClientRegistry.register(id, renderer);
    }
}
