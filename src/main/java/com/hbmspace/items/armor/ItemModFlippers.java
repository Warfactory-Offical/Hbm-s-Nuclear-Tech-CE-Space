package com.hbmspace.items.armor;

import com.hbm.handler.ArmorModHandler;
import com.hbm.items.IDynamicModels;
import com.hbm.items.ModItems;
import com.hbm.items.armor.ArmorEnvsuit;
import com.hbm.items.armor.ItemArmorMod;
import com.hbm.items.gear.ArmorFSB;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModFlippers extends ItemArmorMod implements IDynamicModelsSpace {

    // :o_  flipows

    public ItemModFlippers(String s) {
        super(ArmorModHandler.boots_only, false, false, false, true, s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.BLUE + "Increases swim speed");
        tooltip.add("");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void addDesc(List<String> list, ItemStack stack, ItemStack armor) {
        list.add(TextFormatting.DARK_PURPLE + "  " + stack.getDisplayName() + " (increased swim speed)");
    }

    @Override
    public void modUpdate(EntityLivingBase entity, ItemStack armor) {
        if(entity instanceof EntityPlayer player) {

            if(armor.getItem() instanceof ArmorEnvsuit) {
                if(ArmorFSB.hasFSBArmor(player)) return;
            }

            if(entity.isInWater()) {
                double mo = 0.08 * player.moveForward;
                Vec3d vec = entity.getLookVec();

                double x = vec.x * mo;
                double y = vec.y * mo;
                double z = vec.z * mo;

                entity.motionX += x;
                entity.motionY += y;
                entity.motionZ += z;
            }
        }
    }

}
