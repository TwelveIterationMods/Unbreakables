package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record CombinedHint(List<? extends BreakHint<?>> hints) implements BreakHint<CombinedHint> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "multiple");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public Serializer<CombinedHint> serializer() {
        return CombinedBreakHintSerializer.INSTANCE;
    }

    public static class CombinedBreakHintSerializer implements Serializer<CombinedHint> {

        public static final CombinedBreakHintSerializer INSTANCE = new CombinedBreakHintSerializer();

        @SuppressWarnings("unchecked")
        @Override
        public void encode(RegistryFriendlyByteBuf buf, CombinedHint hint) {
            buf.writeVarInt(hint.hints().size());
            for (final var child : hint.hints()) {
                buf.writeResourceLocation(child.id());
                @SuppressWarnings("rawtypes") final var serializer = (Serializer) child.serializer();
                serializer.encode(buf, child);
            }
        }

        @Override
        public CombinedHint decode(RegistryFriendlyByteBuf buf) {
            final var size = buf.readVarInt();
            final List<BreakHint<?>> hints = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                final var id = buf.readResourceLocation();
                final var serializer = BreakHintRegistry.getSerializer(id);
                final var hint = serializer.decode(buf);
                hints.add(hint);
            }
            return new CombinedHint(hints);
        }
    }
}
