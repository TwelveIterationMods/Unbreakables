package net.blay09.mods.unbreakables.api;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface BreakHint<T extends BreakHint<T>> {

    ResourceLocation id();

    Serializer<T> serializer();

    interface Serializer<T> {
        void encode(RegistryFriendlyByteBuf buf, T hint);

        T decode(RegistryFriendlyByteBuf buf);
    }
}
