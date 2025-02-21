package net.blay09.mods.unbreakables.api.client;

import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.resources.ResourceLocation;

public interface InternalClientMethods {
    <T extends BreakHint<T>> void registerHintRenderer(ResourceLocation id, BreakHintRenderer<T> renderer);
}
