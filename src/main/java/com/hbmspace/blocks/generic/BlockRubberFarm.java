package com.hbmspace.blocks.generic;

import com.google.common.collect.ImmutableMap;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BlockRubberFarm extends BlockFarmland implements IDynamicModelsSpace {

    public BlockRubberFarm(String s) {
        super();
        this.setTranslationKey(s);
        this.setRegistryName(s);
        this.setHarvestLevel("pickaxe", 0);
        this.setCreativeTab(MainRegistry.controlTab);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public void updateTick(@NotNull World world, @NotNull BlockPos pos, IBlockState state, @NotNull Random random) {
        int moisture = state.getValue(MOISTURE);
        if (!this.isWaterNearby(world, pos) && !world.isRainingAt(pos.up())) {
            if (moisture > 0) {
                world.setBlockState(pos, state.withProperty(MOISTURE, 0), 2);
            }
        } else {
            if (moisture < 7) {
                world.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
            }
        }
    }

    // func_149821_m ported
    private boolean isWaterNearby(World world, BlockPos pos) {
        for (BlockPos.MutableBlockPos mutablePos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            IBlockState nearbyState = world.getBlockState(mutablePos);
            // Check for custom block OR vanilla water (as per comment in original code)
            if (nearbyState.getBlock() == ModBlocksSpace.ccl_block || nearbyState.getMaterial() == Material.WATER) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onFallenUpon(World world, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (!world.isRemote && world.rand.nextFloat() < fallDistance - 0.5F) {
            if (!(entity instanceof EntityPlayer) && !world.getGameRules().getBoolean("mobGriefing")) {
                return;
            }
            // Revert to rubber silt
            world.setBlockState(pos, ModBlocksSpace.rubber_silt.getDefaultState());
        }
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        if (world.getBlockState(pos.up()).getMaterial().isSolid()) {
            world.setBlockState(pos, ModBlocksSpace.rubber_silt.getDefaultState());
        }
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return ModBlocksSpace.rubber_silt.getItemDropped(ModBlocksSpace.rubber_silt.getDefaultState(), rand, fortune);
    }

    @Override
    public boolean canSustainPlant(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing direction, @NotNull IPlantable plantable) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprite(TextureMap map) {
        map.registerSprite(new ResourceLocation("hbm", "blocks/rubber_farmland"));
        map.registerSprite(new ResourceLocation("hbm", "blocks/rubber_farmland_moist"));
        map.registerSprite(new ResourceLocation("hbm", "blocks/rubber_silt"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bakeModel(ModelBakeEvent event) {
        try {
            IModel modelDryBase = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/farmland"));
            IModel modelWetBase = ModelLoaderRegistry.getModel(new ResourceLocation("minecraft:block/farmland_moist"));

            String texSilt = "hbm:blocks/rubber_silt";
            String texDry = "hbm:blocks/rubber_farmland";
            String texWet = "hbm:blocks/rubber_farmland_moist";

            ImmutableMap<String, String> texturesDry = ImmutableMap.of(
                    "dirt", texSilt,
                    "top", texDry,
                    "particle", texSilt
            );
            IBakedModel bakedDry = modelDryBase.retexture(texturesDry).bake(ModelRotation.X0_Y0, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            ImmutableMap<String, String> texturesWet = ImmutableMap.of(
                    "dirt", texSilt,
                    "top", texWet,
                    "particle", texSilt
            );
            IBakedModel bakedWet = modelWetBase.retexture(texturesWet).bake(ModelRotation.X0_Y0, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());

            event.getModelRegistry().putObject(new ModelResourceLocation(this.getRegistryName(), "moisture=0"), bakedDry);
            event.getModelRegistry().putObject(new ModelResourceLocation(this.getRegistryName(), "moisture=7"), bakedWet);

            // Inventory model
            event.getModelRegistry().putObject(new ModelResourceLocation(this.getRegistryName(), "inventory"), bakedDry);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public StateMapperBase getStateMapper(ResourceLocation loc) {
        return new StateMapperBase() {
            @Override
            protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
                int moisture = state.getValue(MOISTURE);
                String variant = moisture > 0 ? "moisture=7" : "moisture=0";
                return new ModelResourceLocation(loc, variant);
            }
        };
    }
}
