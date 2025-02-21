package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record MessageHint(Component component) implements BreakHint<MessageHint> {

    public static final ResourceLocation ID = new ResourceLocation(Unbreakables.MOD_ID, "message");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public Serializer<MessageHint> serializer() {
        return MessageHintSerializer.INSTANCE;
    }

    public static class MessageHintSerializer implements Serializer<MessageHint> {

        public static final MessageHintSerializer INSTANCE = new MessageHintSerializer();

        @Override
        public void encode(FriendlyByteBuf buf, MessageHint hint) {
            buf.writeComponent(hint.component());
        }

        @Override
        public MessageHint decode(FriendlyByteBuf buf) {
            return new MessageHint(buf.readComponent());
        }
    }
}
