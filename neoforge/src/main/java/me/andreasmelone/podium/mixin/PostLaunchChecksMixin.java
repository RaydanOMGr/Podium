package me.andreasmelone.podium.mixin;

import net.caffeinemc.mods.sodium.client.compatibility.checks.PostLaunchChecks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PostLaunchChecks.class)
public class PostLaunchChecksMixin {
    @Inject(
            at = @At("HEAD"),
            method = "isUsingPojavLauncher()Z",
            cancellable = true,
            remap = false
    )
    private static void isUsingPojavMixin(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
