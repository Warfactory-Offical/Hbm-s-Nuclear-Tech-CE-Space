package com.hbmspace.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class HBMSpaceSoundHandler {
    public static SoundEvent PLSS_BREATHING;

    // На случай если кто-то запросит раньше инъекции — ленивый lookup по реестру
    public static SoundEvent getPlssBreathing() {
        if (PLSS_BREATHING == null) {
            PLSS_BREATHING = ForgeRegistries.SOUND_EVENTS.getValue(
                    new ResourceLocation("hbm", "player.plss_breathing")
            );
        }
        return PLSS_BREATHING;
    }

    private HBMSpaceSoundHandler() {}
}
