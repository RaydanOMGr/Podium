package me.andreasmelone.podium.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.caffeinemc.mods.sodium.client.compatibility.checks;

@Mixin(PostLaunchChecks.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "isUsingPojavLauncher()V")
	private void isUsingPojavMixin(CallbackInfoReturnable<Boolean> cir) {
    cir.setReturnValue(false);
	}
}
