package com.hbmspace.items.food;

import com.google.common.collect.ImmutableMap;
import com.hbm.capability.HbmLivingProps;
import com.hbm.config.VersatileConfig;
import com.hbm.interfaces.Spaghetti;
import com.hbm.items.ModItems;
import com.hbm.items.food.ItemEnergy;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import static com.hbm.items.ItemEnumMulti.ROOT_PATH;

public class ItemEnergySpace extends ItemEnergy implements IDynamicModelsSpace {
    String texturePath;

    public ItemEnergySpace(String s) {
        super(s);
        texturePath = s;
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    public ItemEnergySpace(String s, String tex) {
        super(s);
        texturePath = tex;
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Spaghetti("clusterfuck out of my own laziness")
    @Override
    public @NotNull ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entity) {
        if(!worldIn.isRemote && entity instanceof EntityPlayer player) {
            if (player instanceof FakePlayer) {
                worldIn.newExplosion(player, player.posX, player.posY, player.posZ, 5F, true, true);
                return super.onItemUseFinish(stack, worldIn, entity);
            }
            if (player instanceof EntityPlayerMP playerMP) {
                CriteriaTriggers.CONSUME_ITEM.trigger(playerMP, stack);
            }
            VersatileConfig.applyPotionSickness(player, 5);

            if(this == ModItemsSpace.glass_smilk) {
                player.heal(6F); //ideas welcome pls thanks
            }
            if(this == ModItemsSpace.teacup) {
                player.heal(3F);
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 30 * 20, 4));
            }
            if(this == ModItemsSpace.bottle_honey) {
                player.heal(9F);  //sweet sorrow
                double digamma = HbmLivingProps.getDigamma(player);
                HbmLivingProps.setDigamma(player, Math.max(digamma - 0.3F, 0F));
            }

            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
            player.inventoryContainer.detectAndSendChanges();
        }
        return stack;
    }

    @Override
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/generated"));
            ResourceLocation spriteLoc = new ResourceLocation("hbm", ROOT_PATH + texturePath);
            IModel retexturedModel = baseModel.retexture(
                    ImmutableMap.of(
                            "layer0", spriteLoc.toString()
                    )

            );
            IBakedModel bakedModel = retexturedModel.bake(ModelRotation.X0_Y0, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
            ModelResourceLocation bakedModelLocation = new ModelResourceLocation(spriteLoc, "inventory");
            event.getModelRegistry().putObject(bakedModelLocation, bakedModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation("hbm", ROOT_PATH + texturePath), "inventory"));
    }

    @Override
    public void registerSprite(TextureMap map) {
        map.registerSprite(new ResourceLocation("hbm", ROOT_PATH + texturePath));
    }
}
