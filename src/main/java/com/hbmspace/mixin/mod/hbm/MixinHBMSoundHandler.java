package com.hbmspace.mixin.mod.hbm;

import com.hbm.lib.HBMSoundHandler;
import com.hbmspace.lib.HBMSpaceSoundHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HBMSoundHandler.class, remap = false)
public abstract class MixinHBMSoundHandler {

    @Inject(method = "init", at = @At("TAIL"))
    private static void myaddon$injectNewSound(CallbackInfo ci) {
        HBMSpaceSoundHandler.PLSS_BREATHING = HBMSoundHandler.register("player.plss_breathing");
    }
}
