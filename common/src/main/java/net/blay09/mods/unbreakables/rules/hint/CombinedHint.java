package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record CombinedHint(List<? extends BreakHint<?>> hints) implements BreakHint<CombinedHint> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "multiple");

    @Override
    public Identifier id() {
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
                buf.writeIdentifier(child.id());
                @SuppressWarnings("rawtypes") final var serializer = (Serializer) child.serializer();
                serializer.encode(buf, child);
            }
        }

        @Override
        public CombinedHint decode(RegistryFriendlyByteBuf buf) {
            final var size = buf.readVarInt();
            final List<BreakHint<?>> hints = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                final var id = buf.readIdentifier();
                final var serializer = BreakHintRegistry.getSerializer(id);
                final var hint = serializer.decode(buf);
                hints.add(hint);
            }
            return new CombinedHint(hints);
        }
    }
}
