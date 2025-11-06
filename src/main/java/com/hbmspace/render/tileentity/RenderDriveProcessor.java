package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineDriveProcessor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderDriveProcessor extends TileEntitySpecialRenderer<TileEntityMachineDriveProcessor> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityMachineDriveProcessor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            GlStateManager.rotate(90F, 0F, 1F, 0F);

            switch (te.getBlockMetadata() - BlockDummyable.offset) {
                case 2 -> GlStateManager.rotate(0F, 0F, 1F, 0F);
                case 4 -> GlStateManager.rotate(90F, 0F, 1F, 0F);
                case 3 -> GlStateManager.rotate(180F, 0F, 1F, 0F);
                case 5 -> GlStateManager.rotate(270F, 0F, 1F, 0F);
            }

            bindTexture(ResourceManagerSpace.drive_processor_tex);
            ResourceManagerSpace.drive_processor.renderPart("Base");

            if (te.hasDrive)
                ResourceManagerSpace.drive_processor.renderPart("Drive");

            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                // GlStateManager.translate(0, -0.5, 0);
                GlStateManager.scale(7F, 7F, 7F);
            }
            public void renderCommon() {
                GlStateManager.translate(0.5D, 0D, 0D);
                GlStateManager.shadeModel(GL11.GL_SMOOTH);

                bindTexture(ResourceManagerSpace.drive_processor_tex);
                ResourceManagerSpace.drive_processor.renderPart("Base");
                ResourceManagerSpace.drive_processor.renderPart("Drive");

                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.machine_drive_processor);
    }
}
