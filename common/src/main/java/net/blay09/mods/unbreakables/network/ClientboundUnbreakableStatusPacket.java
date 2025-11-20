package net.blay09.mods.unbreakables.network;

import net.blay09.mods.unbreakables.BreakContextImpl;
import net.blay09.mods.unbreakables.BreakTracker;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.blay09.mods.unbreakables.rules.hint.BreakHintRegistry;
import net.blay09.mods.unbreakables.rules.requirements.ServersideResponseRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public record ClientboundUnbreakableStatusPacket(BlockPos pos, BreakHint<?> hint, boolean breakable) implements CustomPacketPayload {

    public static Type<ClientboundUnbreakableStatusPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "unbreakable_status"));

    public static StreamCodec<RegistryFriendlyByteBuf, ClientboundUnbreakableStatusPacket> STREAM_CODEC = StreamCodec.of(
            ClientboundUnbreakableStatusPacket::encode,
            ClientboundUnbreakableStatusPacket::decode
    );

    @SuppressWarnings("unchecked")
    public static void encode(RegistryFriendlyByteBuf buf, ClientboundUnbreakableStatusPacket message) {
        buf.writeBlockPos(message.pos);
        buf.writeBoolean(message.breakable);
        buf.writeIdentifier(message.hint.id());
        @SuppressWarnings("rawtypes") final var serializer = (BreakHint.Serializer) message.hint.serializer();
        serializer.encode(buf, message.hint);
    }

    public static ClientboundUnbreakableStatusPacket decode(RegistryFriendlyByteBuf buf) {
        final var pos = buf.readBlockPos();
        final var unbreakable = buf.readBoolean();
        final var hintId = buf.readIdentifier();
        final var hintSerializer = BreakHintRegistry.getSerializer(hintId);
        final var hint = hintSerializer.decode(buf);
        return new ClientboundUnbreakableStatusPacket(pos, hint, unbreakable);
    }

    public static void handle(Player player, ClientboundUnbreakableStatusPacket message) {
        BreakTracker.getContext(player, message.pos)
                .ifPresent(context -> ((BreakContextImpl) context).resolve(new ServersideResponseRequirement(message.hint(), message.breakable)));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
