package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.Balm;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class CooldownTracker {

    private static final String COOLDOWNS = "Cooldowns";

    public static CompoundTag getUnbreakablesData(Player player) {
        final var playerData = Balm.hooks().getPersistentData(player);
        final var unbreakablesData = playerData.getCompoundOrEmpty(Unbreakables.MOD_ID);
        playerData.put(Unbreakables.MOD_ID, unbreakablesData);
        return unbreakablesData;
    }

    public static long getCooldownUntil(Player player, Identifier id) {
        final var data = getUnbreakablesData(player);
        return data.getCompound(COOLDOWNS).flatMap(it -> it.getLong(id.toString())).orElse(0L);
    }

    public static long getCooldownMillisLeft(Player player, Identifier id) {
        long cooldownUntil = getCooldownUntil(player, id);
        return Math.max(0, cooldownUntil - System.currentTimeMillis());
    }

    public static void setCooldownUntil(Player player, Identifier id, long timestamp) {
        final var data = getUnbreakablesData(player);
        final var cooldowns = data.getCompoundOrEmpty(COOLDOWNS);
        cooldowns.putLong(id.toString(), timestamp);
        data.put(COOLDOWNS, cooldowns);
    }
}
