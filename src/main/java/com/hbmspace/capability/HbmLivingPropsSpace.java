package com.hbmspace.capability;

import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.lib.ModDamageSourceSpace;
import net.minecraft.entity.EntityLivingBase;

public class HbmLivingPropsSpace {

    public static HbmLivingCapabilitySpace.IEntityHbmProps getData(EntityLivingBase entity) {
        return entity.hasCapability(HbmLivingCapabilitySpace.EntityHbmPropsProvider.ENT_HBM_PROPS_CAP, null)
                ? entity.getCapability(HbmLivingCapabilitySpace.EntityHbmPropsProvider.ENT_HBM_PROPS_CAP, null)
                : HbmLivingCapabilitySpace.EntityHbmPropsProvider.DUMMY;
    }

    //ATMOSPHERE//
    public static int getOxy(EntityLivingBase entity) {
        return getData(entity).getOxy();
    }

    public static void setOxy(EntityLivingBase entity, int oxygen) {
        if(oxygen <= 0) {
            oxygen = 0;

            // Only damage every 4 ticks, giving the player more time to react
            if(entity.ticksExisted % 4 == 0) {
                entity.attackEntityFrom(ModDamageSourceSpace.oxyprime, 1);
            }
        }

        getData(entity).setOxy(oxygen);
    }

    public static CBT_Atmosphere getAtmosphere(EntityLivingBase entity) {
        return getData(entity).getAtmosphere();
    }

    public static void setAtmosphere(EntityLivingBase entity, CBT_Atmosphere atmosphere) {
        getData(entity).setAtmosphere(atmosphere);
    }

    // and gravity (attached to atmospheres, for now)
    public static boolean hasGravity(EntityLivingBase entity) {
        return getData(entity).hasGravity();
    }

}
