package com.hbmspace.lib;

import net.minecraft.util.DamageSource;

public class ModDamageSourceSpace {

    public static DamageSource eve = (new DamageSource("eve")).setDamageIsAbsolute().setDamageBypassesArmor();
    public static DamageSource oxyprime = (new DamageSource("oxyprime")).setDamageIsAbsolute().setDamageBypassesArmor();
    public static DamageSource run = (new DamageSource("run")).setDamageIsAbsolute().setDamageBypassesArmor();
}
