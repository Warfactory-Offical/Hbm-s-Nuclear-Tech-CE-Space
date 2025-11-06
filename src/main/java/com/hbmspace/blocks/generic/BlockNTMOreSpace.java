package com.hbmspace.blocks.generic;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.OreEnumUtil;
import com.hbm.blocks.generic.BlockNTMOre;
import com.hbmspace.blocks.ModBlocksSpace;

public class BlockNTMOreSpace extends BlockNTMOre {

    public BlockNTMOreSpace(String name, int harvestLvl) {
        super(name, (OreEnumUtil.OreEnum)null, harvestLvl, 2);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
    }

}
