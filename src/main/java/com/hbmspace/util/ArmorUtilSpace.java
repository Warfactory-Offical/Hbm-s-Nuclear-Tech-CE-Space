package com.hbmspace.util;

import com.hbm.handler.ArmorModHandler;
import com.hbm.items.gear.ArmorFSB;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.PlayerInformPacketLegacy;
import com.hbm.util.ChatBuilder;
import com.hbmspace.accessors.ICanSealAccessor;
import com.hbmspace.accessors.IHaveCorrosionProtAccessor;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.items.armor.ItemModOxy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;

import java.util.Random;

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

    public static boolean checkForCorrosion(EntityLivingBase entity, CBT_Atmosphere atmosphere) {
        if(!ChunkAtmosphereManager.proxy.willCorrode(atmosphere)) return false;

        if(!(entity instanceof EntityPlayer player)) return true;

        ItemStack insert = getCorrosionProtection(player);
        if(insert.isEmpty()) {
            boolean isSealed = true; // safe, for now...
            Random rand = entity.getRNG();

            // if we have a full set of armor, deplete that rapidly first before applying damage
            for(int i = 0; i < 4; i++) {
                ItemStack stack = player.inventory.armorInventory.get(i);
                if(stack.isEmpty() || !(stack.getItem() instanceof ICanSealAccessor) || !((ICanSealAccessor)stack.getItem()).getCanSeal()) {
                    isSealed = false;
                }

                if(stack.isEmpty()) continue;

                if(rand.nextInt(2) == 0) {
                    stack.setItemDamage(stack.getItemDamage() + 1);
                }

                if(stack.getItemDamage() >= stack.getMaxDamage() || !(stack.getItem() instanceof ArmorFSB) || !((ArmorFSB)stack.getItem()).isArmorEnabled(stack)) {
                    stack.setCount(0);
                    player.inventory.armorInventory.set(i, ItemStack.EMPTY);
                    entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.2F, 1F);
                }
            }

            if(isSealed && entity instanceof EntityPlayerMP) {
                PacketDispatcher.wrapper.sendTo(new PlayerInformPacketLegacy(ChatBuilder.start("").nextTranslation("info.corrosion").color(TextFormatting.RED).flush(), 12, 3000), (EntityPlayerMP) entity);
            }

            return !isSealed;
        }

        return false;
    }

    public static ItemStack getCorrosionProtection(EntityPlayer player) {
        // Check chest piece has a valid insert
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if(!chest.isEmpty() && ArmorModHandler.hasMods(chest)) {
            ItemStack insertMod = ArmorModHandler.pryMods(chest)[ArmorModHandler.kevlar];
            if(insertMod.isEmpty() || !(insertMod.getItem() instanceof IHaveCorrosionProtAccessor)) return ItemStack.EMPTY;
            if(!((IHaveCorrosionProtAccessor)insertMod.getItem()).getHaveCorProt()) return ItemStack.EMPTY;

            return insertMod;
        }

        return ItemStack.EMPTY;
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
