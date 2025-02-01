package net.blay09.mods.unbreakables.network;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.rulesets.RulesetLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class UnbreakableRulesMessage implements CustomPacketPayload {

    public static Type<UnbreakableRulesMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "unbreakable_rules"));

    private final List<String> rules;

    public UnbreakableRulesMessage(List<String> rules) {
        this.rules = rules;
    }

    public static void encode(FriendlyByteBuf buf, UnbreakableRulesMessage message) {
        buf.writeVarInt(message.rules.size());
        for (String rule : message.rules) {
            buf.writeUtf(rule);
        }
    }

    public static UnbreakableRulesMessage decode(FriendlyByteBuf buf) {
        final var count = buf.readVarInt();
        final var rules = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            rules.add(buf.readUtf());
        }
        return new UnbreakableRulesMessage(rules);
    }

    public static void handle(Player player, UnbreakableRulesMessage message) {
        RulesetLoader.reset();
        RulesetLoader.load(message.rules);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
