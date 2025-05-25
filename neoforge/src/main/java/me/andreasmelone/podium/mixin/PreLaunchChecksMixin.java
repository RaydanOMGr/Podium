package me.andreasmelone.podium.mixin;

import net.caffeinemc.mods.sodium.client.compatibility.checks.PreLaunchChecks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PreLaunchChecks.class)
public abstract class PreLaunchChecksMixin {
    @Shadow
    private static boolean isUsingPrismLauncher() { return false; }

    // removing this check may cause issues outside pojav
    @Inject(
            method = "checkLwjglRuntimeVersion()V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void checkLwjglRuntimeVersionMixin(CallbackInfo ci) {
        // don't cancel if prism is detected
        // this means that if somehow, a prism user ends up with podium and the wrong lwjgl version
        // we don't need to give them a weird crash message because some non-existant method has been
        // called, but instead give them the default lwjgl mismatch message
        if(!isUsingPrismLauncher()) ci.cancel();
    }
}
