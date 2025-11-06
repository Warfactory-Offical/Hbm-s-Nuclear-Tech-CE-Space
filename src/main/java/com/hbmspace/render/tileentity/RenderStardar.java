package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineStardar;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderStardar extends TileEntitySpecialRenderer<TileEntityMachineStardar> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityMachineStardar stardar, double x, double y, double z, float interp, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y - 3D, z + 0.5D);
            GL11.glEnable(GL11.GL_LIGHTING);

            GlStateManager.pushMatrix();
            {

                GlStateManager.rotate(180, 0F, 1F, 0F);

                switch (stardar.getBlockMetadata() - BlockDummyable.offset) {
                    case 2 -> GlStateManager.rotate(0, 0F, 1F, 0F);
                    case 4 -> GlStateManager.rotate(90, 0F, 1F, 0F);
                    case 3 -> GlStateManager.rotate(180, 0F, 1F, 0F);
                    case 5 -> GlStateManager.rotate(270, 0F, 1F, 0F);
                }

                bindTexture(ResourceManagerSpace.stardar_tex);

                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                ResourceManagerSpace.stardar.renderPart("base");

            }
            GlStateManager.popMatrix();

            float dishYaw = stardar.prevDishYaw + (stardar.dishYaw - stardar.prevDishYaw) * interp;
            float dishPitch = stardar.prevDishPitch + (stardar.dishPitch - stardar.prevDishPitch) * interp;
            float dishOffset = 10.6F;

            GlStateManager.rotate(dishYaw, 0, 1, 0);
            ResourceManagerSpace.stardar.renderPart("rotation");

            GlStateManager.shadeModel(GL11.GL_FLAT);

            GlStateManager.translate(0, dishOffset, 0);
            GlStateManager.rotate(dishPitch, 1, 0, 0);
            GlStateManager.translate(0, -dishOffset, 0);
            ResourceManagerSpace.stardar.renderPart("pitch");

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0, -5, 0);
                GlStateManager.scale(1.5D, 1.5D, 1.5D);
            }
            public void renderCommon() {
                GlStateManager.scale(0.55, 0.55, 0.55);
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.stardar_tex);
                ResourceManagerSpace.stardar.renderAll();
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.enableCull();
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_stardar);
    }

}
