package net.blay09.mods.unbreakables.client.hint;

import com.mojang.blaze3d.platform.Window;
import net.blay09.mods.unbreakables.Unbreakables;
import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.blay09.mods.unbreakables.rules.hint.ItemHint;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ItemHintRenderer implements BreakHintRenderer<ItemHint> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Unbreakables.MOD_ID, "textures/gui/icons.png");

    @Override
    public void render(Window window, GuiGraphics guiGraphics, float partialTicks, ItemHint hint) {
        final var font = Minecraft.getInstance().font;
        final var canAfford = hint.canAfford();
        final var component = Component.translatable("gui.unbreakables.item", hint.count(), hint.itemStack().getHoverName())
                .withStyle(canAfford ? ChatFormatting.GREEN : ChatFormatting.RED);
        final var textWidth = font.width(component);
        final var x = window.getGuiScaledWidth() / 2 - 8 - textWidth / 2;
        final var y = window.getGuiScaledHeight() / 2 - 8 + 16;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 48, 0, 16, 16, 256, 256);
        final var poseStack = guiGraphics.pose();
        poseStack.pushMatrix();
        poseStack.translate(x + 5, y + 7); // TODO z 1
        poseStack.scale(0.5f, 0.5f);
        guiGraphics.renderFakeItem(hint.itemStack(), 0, 0);
        poseStack.popMatrix();
        if (!canAfford) {
            poseStack.pushMatrix();
            // TODO z 302
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 64, 0, 16, 16, 256, 256);
            poseStack.popMatrix();
        }
        guiGraphics.drawString(font, component, x + 16 + 4, y + 8 - font.lineHeight / 2 + 1, 0xFFFFFFFF);
    }
}
