package net.blay09.mods.unbreakables.rules.requirements;

import net.blay09.mods.unbreakables.api.BreakContext;
import net.blay09.mods.unbreakables.api.BreakRequirement;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.blay09.mods.unbreakables.rules.hint.ItemHint;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ItemRequirement implements BreakRequirement {

    private ItemStack itemStack;
    private int count;

    public ItemRequirement(ItemStack item, int count) {
        this.itemStack = item;
        this.count = count;
    }

    @Override
    public boolean canAfford(BreakContext context, Player player) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            final var slotStack = player.getInventory().getItem(i);
            if (ItemStack.isSameItemSameTags(itemStack, slotStack)) {
                count += slotStack.getCount();

                if (count >= this.count) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void consume(Player player) {
        var consumed = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            final var leftToConsume = this.count - consumed;
            final var slotStack = player.getInventory().getItem(i);
            if (ItemStack.isSameItemSameTags(itemStack, slotStack)) {
                final var count = Math.min(slotStack.getCount(), leftToConsume);
                slotStack.shrink(count);
                consumed += count;

                if (consumed >= this.count) {
                    return;
                }
            }
        }
    }

    @Override
    public void rollback(Player player) {
        var added = 0;
        while (added < count) {
            final var leftToAdd = count - added;
            final var itemStack = this.itemStack.copy();
            itemStack.setCount(Math.min(itemStack.getMaxStackSize(), leftToAdd));
            if (!player.addItem(itemStack)) {
                player.drop(itemStack, false, false);
            }
            added += itemStack.getCount();
        }
    }

    @Override
    public boolean isEmpty() {
        return itemStack.isEmpty() || count <= 0;
    }

    @Override
    public Optional<BreakHint<?>> hint(BreakContext context, Player player) {
        return Optional.of(new ItemHint(itemStack, count, canAfford(context, player)));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
