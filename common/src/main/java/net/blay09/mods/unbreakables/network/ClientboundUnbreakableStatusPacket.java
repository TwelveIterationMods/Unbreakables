package net.blay09.mods.unbreakables.network;

import net.blay09.mods.unbreakables.BreakContextImpl;
import net.blay09.mods.unbreakables.BreakTracker;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.blay09.mods.unbreakables.rules.hint.BreakHintRegistry;
import net.blay09.mods.unbreakables.rules.requirements.ServersideResponseRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record ClientboundUnbreakableStatusPacket(BlockPos pos, BreakHint<?> hint, boolean breakable) {

    @SuppressWarnings("unchecked")
    public static void encode(ClientboundUnbreakableStatusPacket message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeBoolean(message.breakable);
        buf.writeResourceLocation(message.hint.id());
        @SuppressWarnings("rawtypes") final var serializer = (BreakHint.Serializer) message.hint.serializer();
        serializer.encode(buf, message.hint);
    }

    public static ClientboundUnbreakableStatusPacket decode(FriendlyByteBuf buf) {
        final var pos = buf.readBlockPos();
        final var unbreakable = buf.readBoolean();
        final var hintId = buf.readResourceLocation();
        final var hintSerializer = BreakHintRegistry.getSerializer(hintId);
        final var hint = hintSerializer.decode(buf);
        return new ClientboundUnbreakableStatusPacket(pos, hint, unbreakable);
    }

    public static void handle(Player player, ClientboundUnbreakableStatusPacket message) {
        BreakTracker.getContext(player, message.pos)
                .ifPresent(context -> ((BreakContextImpl) context).resolve(new ServersideResponseRequirement(message.hint(), message.breakable)));
    }

}
