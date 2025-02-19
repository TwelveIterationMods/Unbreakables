package net.blay09.mods.unbreakables.network;

import net.blay09.mods.unbreakables.BreakContextImpl;
import net.blay09.mods.unbreakables.BreakTracker;
import net.blay09.mods.unbreakables.rules.requirements.ServersideResponseRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record ClientboundUnbreakableStatusPacket(BlockPos pos, boolean breakable) {

    public static void encode(ClientboundUnbreakableStatusPacket message, FriendlyByteBuf buf) {
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

}
