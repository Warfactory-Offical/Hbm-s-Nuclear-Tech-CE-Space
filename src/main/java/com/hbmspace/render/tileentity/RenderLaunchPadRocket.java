package com.hbmspace.render.tileentity;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.main.ResourceManagerSpace;
import com.hbmspace.render.misc.RocketPronter;
import com.hbmspace.tileentity.bomb.TileEntityLaunchPadRocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import org.lwjgl.opengl.GL11;
@AutoRegister
public class RenderLaunchPadRocket extends TileEntitySpecialRenderer<TileEntityLaunchPadRocket> implements IItemRendererProviderSpace {

    @Override
    public void render(TileEntityLaunchPadRocket pad, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        {

            GlStateManager.translate(x + 0.5D, y, z + 0.5D);
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            switch (pad.getBlockMetadata() - BlockDummyable.offset) {
                case 2 -> GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                case 4 -> GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                case 3 -> GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
                case 5 -> GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
            }


            bindTexture(ResourceManagerSpace.rocket_pad_tex);
            ResourceManagerSpace.rocket_pad.renderPart("Base");

            GlStateManager.disableCull();

            if(pad.canSeeSky && pad.height >= 8) {
                GlStateManager.pushMatrix();
                {

                    bindTexture(ResourceManagerSpace.rocket_pad_support_tex);
                    ResourceManagerSpace.rocket_pad.renderPart("Tower_Base");

                    for(int oy = 8; oy < pad.height - 2; oy += 3) {
                        ResourceManagerSpace.rocket_pad.renderPart("Tower_Segment");
                        GlStateManager.translate(0.0D, 3.0D, 0.0D);
                    }

                    GlStateManager.translate(0.0D, -3.0D, 0.0D);

                    for(int oy = 0; oy < (pad.height - 2) % 3; oy++) {
                        ResourceManagerSpace.rocket_pad.renderPart("Tower_Segment_Small");
                        GlStateManager.translate(0.0D, 1.0D, 0.0D);
                    }

                    GlStateManager.translate(0.0D, -1.0D, 0.0D);
                    ResourceManagerSpace.rocket_pad.renderPart("Tower_Cap");

                }
                GlStateManager.popMatrix();
            }

            GlStateManager.enableCull();
            GlStateManager.shadeModel(GL11.GL_FLAT);

            if(pad.rocket != null) {
                GlStateManager.translate(0.0D, 3.0D, 0.0D);
                RocketPronter.prontRocket(pad.rocket, Minecraft.getMinecraft().getTextureManager());
            }

        }
        GlStateManager.popMatrix();
    }

    @Override
    public ItemRenderBase getRenderer(Item item) {
        return new ItemRenderBase() {
            public void renderInventory() {
                GlStateManager.translate(0.0D, -2.0D, 0.0D);
                GlStateManager.scale(1.25D, 1.25D, 1.25D);
            }
            public void renderCommon() {
                GlStateManager.scale(0.55D, 0.55D, 0.55D);
                GlStateManager.disableCull();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManagerSpace.rocket_pad_tex);
                ResourceManagerSpace.rocket_pad.renderPart("Base");
                bindTexture(ResourceManagerSpace.rocket_pad_support_tex);
                ResourceManagerSpace.rocket_pad.renderAllExcept("Base");
                GlStateManager.shadeModel(GL11.GL_FLAT);
                GlStateManager.enableCull();
            }
        };
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocksSpace.launch_pad_rocket);
    }

}
