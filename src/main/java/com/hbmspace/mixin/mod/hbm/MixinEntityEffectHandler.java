package com.hbmspace.mixin.mod.hbm;

import com.hbm.entity.mob.EntityCyberCrab;
import com.hbm.entity.mob.glyphid.EntityGlyphid;
import com.hbm.handler.EntityEffectHandler;
import com.hbm.handler.threading.PacketThreading;
import com.hbm.lib.ModDamageSource;
import com.hbmspace.api.entity.ISuffocationImmune;
import com.hbmspace.capability.HbmLivingCapabilitySpace;
import com.hbmspace.capability.HbmLivingPropsSpace;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.entity.missile.EntityRideableRocket;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.packet.toclient.ExtPropSpacePacket;
import com.hbmspace.util.ArmorUtilSpace;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityEffectHandler.class, remap = false)
public class MixinEntityEffectHandler {

    @Inject(method = "onUpdate", at = @At("TAIL"))
    private static void onUpdateSpace(EntityLivingBase entity, CallbackInfo ci) {
        if (!entity.world.isRemote) {
            if(entity instanceof EntityPlayerMP) {
                NBTTagCompound data = new NBTTagCompound();
                HbmLivingCapabilitySpace.IEntityHbmProps props = HbmLivingPropsSpace.getData(entity);
                props.saveNBTData(data);
                PacketThreading.createSendToThreadedPacket(new ExtPropSpacePacket(data), (EntityPlayerMP) entity);
            }
            CBT_Atmosphere atmosphere = getAtmosphereCached(entity);
            handleOxy(entity, atmosphere);
            handleCorrosion(entity, atmosphere);
        }
    }

    @Unique
    private static CBT_Atmosphere getAtmosphereCached(EntityLivingBase entity) {
        // Update non-player entities once per second
        if(entity instanceof EntityPlayerMP || entity.ticksExisted % 20 == 0) {
            CBT_Atmosphere atmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(entity);
            HbmLivingPropsSpace.setAtmosphere(entity, atmosphere);
            return atmosphere;
        }

        return HbmLivingPropsSpace.getAtmosphere(entity);
    }

    @Unique
    private static void handleOxy(EntityLivingBase entity, CBT_Atmosphere atmosphere) {
        if(entity.world.isRemote) return;
        if(entity instanceof ISuffocationImmune) return;
        if(entity.getRidingEntity() != null && entity.getRidingEntity() instanceof EntityRideableRocket) return; // breathe easy in your ship

        if (!ArmorUtilSpace.checkForOxy(entity, atmosphere)) {
            HbmLivingPropsSpace.setOxy(entity, HbmLivingPropsSpace.getOxy(entity) - 1);
        } else {
            HbmLivingPropsSpace.setOxy(entity, 100); // 5 seconds until vacuum damage
        }
    }

    // Corrosive atmospheres melt your suit, without appropriate protection
    @Unique
    private static void handleCorrosion(EntityLivingBase entity, CBT_Atmosphere atmosphere) {
        if(entity.world.isRemote) return;
        if(entity instanceof EntityGlyphid) return;
        if(entity instanceof EntityCyberCrab) return;
        if(entity.getRidingEntity() != null && entity.getRidingEntity() instanceof EntityRideableRocket) return;

        // If we should corrode but we have armor, damage it heavily
        // once it runs out of juice, fizzle it and start damaging the player
        if(ArmorUtilSpace.checkForCorrosion(entity, atmosphere)) {
            entity.attackEntityFrom(ModDamageSource.acid, 1);
        }
    }
}
