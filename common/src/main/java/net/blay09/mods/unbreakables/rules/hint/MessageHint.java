package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;

public record MessageHint(Component component) implements BreakHint<MessageHint> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "message");

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public Serializer<MessageHint> serializer() {
        return MessageHintSerializer.INSTANCE;
    }

    public static class MessageHintSerializer implements Serializer<MessageHint> {

        public static final MessageHintSerializer INSTANCE = new MessageHintSerializer();

        @Override
        public void encode(RegistryFriendlyByteBuf buf, MessageHint hint) {
            ComponentSerialization.STREAM_CODEC.encode(buf, hint.component());
        }

        @Override
        public MessageHint decode(RegistryFriendlyByteBuf buf) {
            return new MessageHint(ComponentSerialization.STREAM_CODEC.decode(buf));
        }
    }
}
