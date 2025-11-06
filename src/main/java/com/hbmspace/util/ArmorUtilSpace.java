package com.hbmspace.util;

import com.hbm.handler.ArmorModHandler;
import com.hbmspace.accessors.ICanSealAccessor;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.items.armor.ItemModOxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ArmorUtilSpace {

    public static boolean checkForOxy(EntityLivingBase entity, CBT_Atmosphere atmosphere) {
        if(!(entity instanceof EntityPlayer player)) return ChunkAtmosphereManager.proxy.canBreathe(atmosphere);

        if(player.capabilities.isCreativeMode || player.isSpectator()) return true;

        ItemStack tank = getOxygenTank(player);
        if(tank.isEmpty()) return ChunkAtmosphereManager.proxy.canBreathe(atmosphere);

        // If we have an oxygen tank, block drowning
        boolean isInWater = entity.getAir() < 300;
        boolean canBreatheTank = ((ItemModOxy)tank.getItem()).attemptBreathing(entity, tank, atmosphere, isInWater);

        if(isInWater && canBreatheTank) {
            entity.setAir(300);
        }

        return canBreatheTank;
    }

    public static ItemStack getOxygenTank(EntityPlayer player) {
        // TODO: only require pressure suits in near vacuums, and use regular oxygen tanks otherwise

        // Check that all the armor pieces are sealed
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getItemStackFromSlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof ICanSealAccessor)) return ItemStack.EMPTY;
                if (!((ICanSealAccessor) stack.getItem()).getCanSeal()) return ItemStack.EMPTY;
            }
        }

        // Check for a non-empty oxygen tank
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        if(!helmet.isEmpty() && ArmorModHandler.hasMods(helmet)) {
            ItemStack tankMod = ArmorModHandler.pryMods(helmet)[ArmorModHandler.plate_only];
            if(tankMod == ItemStack.EMPTY || !(tankMod.getItem() instanceof ItemModOxy)) return ItemStack.EMPTY;

            return tankMod;
        }

        return ItemStack.EMPTY;
    }
}
