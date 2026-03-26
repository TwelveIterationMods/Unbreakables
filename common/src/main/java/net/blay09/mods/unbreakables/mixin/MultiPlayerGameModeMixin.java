package net.blay09.mods.unbreakables.mixin;

import net.blay09.mods.unbreakables.BreakTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "startDestroyBlock", at = @At("HEAD"))
    public void startDestroyBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        final var player = Minecraft.getInstance().player;
        if (player != null) {
            BreakTracker.startBreak(player);
        }
    }

    @Inject(method = "stopDestroyBlock", at = @At("HEAD"))
    public void stopDestroyBlock(CallbackInfo ci) {
        final var player = Minecraft.getInstance().player;
        if (player != null) {
            BreakTracker.stopBreak(player);
        }
    }
}
