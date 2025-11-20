package net.blay09.mods.unbreakables.network;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.rulesets.RulesetLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public record UnbreakableRulesMessage(List<String> rules) implements CustomPacketPayload {

    public static Type<UnbreakableRulesMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "unbreakable_rules"));
    public static StreamCodec<RegistryFriendlyByteBuf, UnbreakableRulesMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
            UnbreakableRulesMessage::rules,
            UnbreakableRulesMessage::new
    );

    public static void handle(Player player, UnbreakableRulesMessage message) {
        RulesetLoader.reset();
        RulesetLoader.load(message.rules);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
