package net.blay09.mods.unbreakables.rules.hint;

import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.BreakHint;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ItemHint(ItemStack itemStack, int count, boolean canAfford) implements BreakHint<ItemHint> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Unbreakables.MOD_ID, "item");

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
        public void encode(RegistryFriendlyByteBuf buf, ItemHint hint) {
            ItemStack.STREAM_CODEC.encode(buf, hint.itemStack());
            buf.writeInt(hint.count());
            buf.writeBoolean(hint.canAfford());
        }

        @Override
        public ItemHint decode(RegistryFriendlyByteBuf buf) {
            return new ItemHint(ItemStack.STREAM_CODEC.decode(buf), buf.readInt(), buf.readBoolean());
        }
    }
}
