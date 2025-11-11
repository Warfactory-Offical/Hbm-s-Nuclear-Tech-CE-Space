package com.hbmspace.inventory;


import com.hbm.inventory.OreDictManager;

import static com.hbmspace.blocks.ModBlocksSpace.block_nickel;
import static com.hbmspace.blocks.ModBlocksSpace.ore_nickel;
import static com.hbmspace.items.ModItemsSpace.*;

public class OreDictManagerSpace {

    /** NICKEL */
    public static final OreDictManager.DictFrame NI = new OreDictManager.DictFrame("NickelPure");

    public static void registerOres(){
        ((IDictFrameAddon) NI.ingot(ingot_nickel).dust(powder_nickel).plate(plate_nickel).block(block_nickel)).oreAll(ore_nickel).nugget(nugget_nickel);
    }
}
