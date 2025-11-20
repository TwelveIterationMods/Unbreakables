package net.blay09.mods.unbreakables.api.client;

import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.resources.Identifier;

public interface InternalClientMethods {
    <T extends BreakHint<T>> void registerHintRenderer(Identifier id, BreakHintRenderer<T> renderer);
}
