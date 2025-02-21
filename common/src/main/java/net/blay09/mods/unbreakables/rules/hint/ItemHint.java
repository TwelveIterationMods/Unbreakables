package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ItemHint(ItemStack itemStack, int count, boolean canAfford) implements BreakHint<ItemHint> {

    public static final ResourceLocation ID = new ResourceLocation(Unbreakables.MOD_ID, "item");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public Serializer<ItemHint> serializer() {
        return ItemHintSerializer.INSTANCE;
    }

    public static class ItemHintSerializer implements Serializer<ItemHint> {
        public static final ItemHintSerializer INSTANCE = new ItemHintSerializer();

        @Override
        public void encode(FriendlyByteBuf buf, ItemHint hint) {
            buf.writeItem(hint.itemStack());
            buf.writeInt(hint.count());
            buf.writeBoolean(hint.canAfford());
        }

        @Override
        public ItemHint decode(FriendlyByteBuf buf) {
            return new ItemHint(buf.readItem(), buf.readInt(), buf.readBoolean());
        }
    }
}
