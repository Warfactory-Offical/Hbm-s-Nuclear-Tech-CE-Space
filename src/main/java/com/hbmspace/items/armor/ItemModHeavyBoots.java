package com.hbmspace.items.armor;

import com.hbm.handler.ArmorModHandler;
import com.hbm.items.IDynamicModels;
import com.hbm.items.ModItems;
import com.hbm.items.armor.ItemArmorMod;
import com.hbm.util.I18nUtil;
import com.hbmspace.dim.CelestialBody;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import com.hbmspace.util.AstronomyUtil;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModHeavyBoots extends ItemArmorMod implements IDynamicModelsSpace {

    public ItemModHeavyBoots(String s) {
        super(ArmorModHandler.boots_only, false, false, false, true, s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.BLUE + "Increases fall speed in low gravity");
        tooltip.add(TextFormatting.BLUE + "Activated by crouching");
        tooltip.add("");
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.GOLD + "Can be worn on its own!");
        tooltip.add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + "We take no responsibility for any deaths that may");
        tooltip.add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + "occur while using these boots without a space suit.");
    }

    @Override
    public void addDesc(List<String> list, ItemStack stack, ItemStack armor) {
        list.add(TextFormatting.DARK_PURPLE + "  " + stack.getDisplayName() + " (" + I18nUtil.resolveKey("armor.fastFall") + ")");
    }

    @Override
    public void onArmorTick(@NotNull World world, EntityPlayer player, @NotNull ItemStack stack) {
        // if crouching in air, apply extra gravity until we match the overworld
        if(player.isSneaking() && !player.onGround && !player.isInWater()) {
            float gravity = CelestialBody.getGravity(player);
            if(gravity > 1.5F) return;
            if(gravity == 0) return;
            if(gravity < 0.2F) gravity = 0.2F;

            player.motionY /= 0.98F;
            player.motionY += (gravity / 20F);
            player.motionY -= (AstronomyUtil.STANDARD_GRAVITY / 20F);
            player.motionY *= 0.98F;
        }
    }

    @Override
    public void modUpdate(EntityLivingBase entity, ItemStack armor) {
        if(!(entity instanceof EntityPlayer))
            return;

        ItemStack boots = ArmorModHandler.pryMods(armor)[ArmorModHandler.boots_only];

        if(boots == null || boots.isEmpty())
            return;

        onArmorTick(entity.world, (EntityPlayer)entity, boots);
        ArmorModHandler.applyMod(armor, boots);
    }

    @Override
    public boolean isValidArmor(@NotNull ItemStack stack, @NotNull EntityEquipmentSlot armorType, @NotNull Entity entity) {
        return armorType == EntityEquipmentSlot.FEET;
    }

    @Override
    public String getArmorTexture(@NotNull ItemStack stack, @NotNull Entity entity, @NotNull EntityEquipmentSlot slot, @NotNull String type) {
        return "hbm:textures/armor/heavy_boots.png";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(@NotNull EntityLivingBase entityLiving, @NotNull ItemStack itemStack, @NotNull EntityEquipmentSlot armorSlot, @NotNull ModelBiped _default) {
        if (armorSlot == EntityEquipmentSlot.FEET) {
            _default.bipedHead.showModel = false;
            _default.bipedHeadwear.showModel = false;
            _default.bipedBody.showModel = false;
            _default.bipedRightArm.showModel = false;
            _default.bipedLeftArm.showModel = false;
            _default.bipedRightLeg.showModel = true;
            _default.bipedLeftLeg.showModel = true;

            _default.isSneak = entityLiving.isSneaking();
            _default.isRiding = entityLiving.isRiding();
            _default.isChild = entityLiving.isChild();

            return _default;
        }
        return null;
    }

}
