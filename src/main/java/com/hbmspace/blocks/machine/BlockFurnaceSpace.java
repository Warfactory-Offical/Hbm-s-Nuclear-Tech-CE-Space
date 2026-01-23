package com.hbmspace.blocks.machine;

import com.google.common.collect.ImmutableMap;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.handler.atmosphere.IBlockSealable;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.tileentity.machine.TileEntityFurnaceSpace;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import org.jetbrains.annotations.NotNull;

public class BlockFurnaceSpace extends BlockFurnace implements IBlockSealable, IDynamicModelsSpace {

    private final boolean isLit;
    private static boolean keepInventory;

    public BlockFurnaceSpace(boolean lit) {
        super(lit);
        this.isLit = lit;
        this.setRegistryName(lit ? "lit_furnace" : "furnace");
        this.setTranslationKey(lit ? "lit_furnace" : "furnace");
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    public static void updateFurnaceBlockState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (active) {
            worldIn.setBlockState(pos, ModBlocksSpace.lit_furnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, ModBlocksSpace.lit_furnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        } else {
            worldIn.setBlockState(pos, ModBlocksSpace.furnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, ModBlocksSpace.furnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        keepInventory = false;

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public @NotNull TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        return new TileEntityFurnaceSpace();
    }

    @Override
    public boolean isSealed(World world, int x, int y, int z) {
        return false;
    }

    @Override
    public void breakBlock(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!keepInventory) {
            super.breakBlock(worldIn, pos, state);
        }
    }

    @Override
    public void bakeModel(ModelBakeEvent event) {
        String front = isLit ? "minecraft:blocks/furnace_front_on" : "minecraft:blocks/furnace_front_off";
        String side = "minecraft:blocks/furnace_side";
        String top = "minecraft:blocks/furnace_top";

        try {
            IModel baseModel = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/cube"));

            ImmutableMap.Builder<String, String> textureMap = ImmutableMap.builder();
            textureMap.put("up", top);
            textureMap.put("down", top);
            textureMap.put("north", front); // Front texture on North face
            textureMap.put("south", side);
            textureMap.put("west", side);
            textureMap.put("east", side);
            textureMap.put("particle", front);

            IModel retexturedModel = baseModel.retexture(textureMap.build());

            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                int y = 0;
                if (facing == EnumFacing.SOUTH) y = 180;
                if (facing == EnumFacing.WEST) y = 270;
                if (facing == EnumFacing.EAST) y = 90;

                TRSRTransformation transform = new TRSRTransformation(ModelRotation.getModelRotation(0, y));
                IBakedModel bakedModel = retexturedModel.bake(transform, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

                ModelResourceLocation modelLocation = new ModelResourceLocation(getRegistryName(), "facing=" + facing.getName());
                event.getModelRegistry().putObject(modelLocation, bakedModel);
            }

            IBakedModel invModel = retexturedModel.bake(ModelRotation.X0_Y0, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());
            event.getModelRegistry().putObject(new ModelResourceLocation(getRegistryName(), "inventory"), invModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void registerSprite(TextureMap map) {
    }
}
