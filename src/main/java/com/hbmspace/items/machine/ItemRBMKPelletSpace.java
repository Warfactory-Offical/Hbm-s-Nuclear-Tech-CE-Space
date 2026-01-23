package com.hbmspace.items.machine;

import com.google.common.collect.ImmutableMap;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemRBMKPellet;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class ItemRBMKPelletSpace extends ItemRBMKPellet implements IDynamicModelsSpace {

    public ItemRBMKPelletSpace(String fullName, String s) {
        super(fullName, s);
        ModItems.ALL_ITEMS.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft", "item/generated"));

            for (int meta = 0; meta < 10; meta++) {
                Map<String, String> textures = new HashMap<>();

                textures.put("layer0", "hbm:items/" + this.getRegistryName().getPath());

                int enrichmentIndex = rectify(meta) % 5;
                textures.put("layer1", "hbm:items/rbmk_pellet_overlay_e" + enrichmentIndex);

                if (hasXenon(meta)) {
                    textures.put("layer2", "hbm:items/rbmk_pellet_overlay_xenon");
                }

                IModel retexturedModel = baseModel.retexture(ImmutableMap.copyOf(textures));
                IBakedModel bakedModel = retexturedModel.bake(retexturedModel.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
                ModelResourceLocation bakedModelLocation = new ModelResourceLocation(this.getRegistryName(), "type=" + meta);
                event.getModelRegistry().putObject(bakedModelLocation, bakedModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        for (int i = 0; i < 10; i++) {
            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(this.getRegistryName(), "type=" + i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        map.registerSprite(new ResourceLocation("hbm", "items/" + this.getRegistryName().getPath()));

        for (int i = 0; i < 5; i++) {
            map.registerSprite(new ResourceLocation("hbm", "items/rbmk_pellet_overlay_e" + i));
        }

        map.registerSprite(new ResourceLocation("hbm", "items/rbmk_pellet_overlay_xenon"));
    }
}
