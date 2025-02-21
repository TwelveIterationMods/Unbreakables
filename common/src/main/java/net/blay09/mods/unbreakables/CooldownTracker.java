package net.blay09.mods.unbreakables;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CooldownTracker {

    private static final String COOLDOWNS = "Cooldowns";

    public static CompoundTag getUnbreakablesData(Player player) {
        final var playerData = Balm.getHooks().getPersistentData(player);
        final var unbreakablesData = playerData.getCompound(Unbreakables.MOD_ID);
        playerData.put(Unbreakables.MOD_ID, unbreakablesData);
        return unbreakablesData;
    }

    public static long getCooldownUntil(Player player, ResourceLocation id) {
        final var data = getUnbreakablesData(player);
        final var cooldowns = data.getCompound(COOLDOWNS);
        return cooldowns.getLong(id.toString());
    }

    public static long getCooldownMillisLeft(Player player, ResourceLocation id) {
        long cooldownUntil = getCooldownUntil(player, id);
        return Math.max(0, cooldownUntil - System.currentTimeMillis());
    }

    public static void setCooldownUntil(Player player, ResourceLocation id, long timestamp) {
        final var data = getUnbreakablesData(player);
        final var cooldowns = data.getCompound(COOLDOWNS);
        cooldowns.putLong(id.toString(), timestamp);
        data.put(COOLDOWNS, cooldowns);
    }
}
