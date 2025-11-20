package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;

public class NoHint implements BreakHint<NoHint> {

    public static final NoHint INSTANCE = new NoHint();
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "none");

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public Serializer<NoHint> serializer() {
        return NoHintSerializer.INSTANCE;
    }

    public static class NoHintSerializer implements Serializer<NoHint> {
        public static final NoHintSerializer INSTANCE = new NoHintSerializer();

        @Override
        public void encode(RegistryFriendlyByteBuf buf, NoHint hint) {
        }

        @Override
        public NoHint decode(RegistryFriendlyByteBuf buf) {
            return NoHint.INSTANCE;
        }
    }
}
