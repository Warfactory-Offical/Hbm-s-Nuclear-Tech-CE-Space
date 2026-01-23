package com.hbmspace.potion;

import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.lib.HBMSoundHandler;
import com.hbmspace.Tags;
import com.hbmspace.lib.ModDamageSourceSpace;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HbmPotion extends Potion {

    public static HbmPotion run;

    public HbmPotion(boolean isBad, int color, String name, int x, int y){
        super(isBad, color);
        this.setPotionName(name);
        this.setRegistryName(Tags.MODID, name);
        this.setIconIndex(x, y);
    }

    public static void init() {
        run = registerPotion(true, 1118481, "potion.hbm_run", 14, 0);
    }

    public static HbmPotion registerPotion(boolean isBad, int color, String name, int x, int y) {

        HbmPotion effect = new HbmPotion(isBad, color, name, x, y);
        ForgeRegistries.POTIONS.register(effect);

        return effect;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex() {
        ResourceLocation loc = new ResourceLocation(Tags.MODID, "textures/gui/potions.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(loc);
        return super.getStatusIconIndex();
    }

    @Override
    public void performEffect(EntityLivingBase entity, int level) {
        if (entity.world.isRemote) return;
        if(this == run) {
            entity.attackEntityFrom(ModDamageSourceSpace.run, 1000);
            entity.setHealth(0.0F);

            new ExplosionVNT(entity.world, entity.posX, entity.posY, entity.posZ, 12).makeAmat().explode();
            entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, HBMSoundHandler.mukeExplosion, SoundCategory.PLAYERS, 100.0F, 1.0F);

            if (!(entity instanceof EntityPlayer))
                entity.setDead();
        }
    }
}
