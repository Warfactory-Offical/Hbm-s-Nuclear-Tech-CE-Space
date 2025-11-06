package com.hbmspace.mixin.mod.hbm;

import com.hbm.items.ModItems;
import com.hbm.items.gear.ArmorFSB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(ModItems.class)
public abstract class MixinModItems {
    // Th3_Sl1ze: apparently, injecting in cloneStats didn't work as it injects later than items are initialized
    // I thought of re-cloning the stats but why the fuck would I need to copy ALL the stats again just for canSeal if I can set the canSeal manually?
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void applySealed(CallbackInfo ci) {
        try {
            Method m = ArmorFSB.class.getMethod("setSealed", boolean.class);
            m.invoke(ModItems.t51_helmet, true);
            m.invoke(ModItems.t51_plate, true);
            m.invoke(ModItems.t51_legs, true);
            m.invoke(ModItems.t51_boots, true);
        } catch (Throwable ignored) {
        }
    }
}
