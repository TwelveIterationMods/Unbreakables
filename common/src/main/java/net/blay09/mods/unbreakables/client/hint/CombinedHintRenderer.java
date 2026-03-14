package net.blay09.mods.unbreakables.client.hint;

import com.mojang.blaze3d.platform.Window;
import net.blay09.mods.unbreakables.api.client.BreakHintRenderer;
import net.blay09.mods.unbreakables.rules.hint.CombinedHint;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class CombinedHintRenderer implements BreakHintRenderer<CombinedHint> {
    @SuppressWarnings("unchecked")
    @Override
    public void render(Window window, GuiGraphicsExtractor guiGraphics, float partialTicks, CombinedHint hint) {
        final var poseStack = guiGraphics.pose();
        poseStack.pushMatrix();
        for (final var child : hint.hints()) {
            @SuppressWarnings("rawtypes") final var childRenderer = (BreakHintRenderer) BreakHintClientRegistry.getRenderer(child.id());
            if (childRenderer != null) {
                childRenderer.render(window, guiGraphics, partialTicks, child);
                poseStack.translate(0f, 16f);
            }
        }
        poseStack.popMatrix();
    }
}
