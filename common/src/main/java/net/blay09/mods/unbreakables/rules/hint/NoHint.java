package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class NoHint implements BreakHint<NoHint> {

    public static final NoHint INSTANCE = new NoHint();
    public static final ResourceLocation ID = new ResourceLocation(Unbreakables.MOD_ID, "none");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public Serializer<NoHint> serializer() {
        return NoHintSerializer.INSTANCE;
    }

    public static class NoHintSerializer implements Serializer<NoHint> {
        public static final NoHintSerializer INSTANCE = new NoHintSerializer();

        @Override
        public void encode(FriendlyByteBuf buf, NoHint hint) {
        }

        @Override
        public NoHint decode(FriendlyByteBuf buf) {
            return NoHint.INSTANCE;
        }
    }
}
