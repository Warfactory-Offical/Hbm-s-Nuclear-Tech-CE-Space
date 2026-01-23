package com.hbmspace.blocks.fluid;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.fluid.GenericFluidBlock;
import com.hbmspace.blocks.ModBlocksSpace;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class GenericFluidBlockSpace extends GenericFluidBlock {

    public GenericFluidBlockSpace(Fluid fluid, Material material, String s) {
        super(fluid, material, s);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
    }
}
