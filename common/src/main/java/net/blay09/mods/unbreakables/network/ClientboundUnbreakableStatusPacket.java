package net.blay09.mods.unbreakables.network;

import net.blay09.mods.unbreakables.BreakContextImpl;
import net.blay09.mods.unbreakables.BreakTracker;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.rules.requirements.ServersideResponseRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ClientboundUnbreakableStatusPacket(BlockPos pos, boolean breakable) implements CustomPacketPayload {

    public static Type<ClientboundUnbreakableStatusPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "unbreakable_status"));

    public static void encode(FriendlyByteBuf buf, ClientboundUnbreakableStatusPacket message) {
        buf.writeBlockPos(message.pos);
        buf.writeBoolean(message.breakable);
    }

    public static ClientboundUnbreakableStatusPacket decode(FriendlyByteBuf buf) {
        final var pos = buf.readBlockPos();
        final var unbreakable = buf.readBoolean();
        return new ClientboundUnbreakableStatusPacket(pos, unbreakable);
    }

    public static void handle(Player player, ClientboundUnbreakableStatusPacket message) {
        BreakTracker.getContext(player, message.pos)
                .ifPresent(context -> ((BreakContextImpl) context).resolve(new ServersideResponseRequirement(message.breakable)));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
