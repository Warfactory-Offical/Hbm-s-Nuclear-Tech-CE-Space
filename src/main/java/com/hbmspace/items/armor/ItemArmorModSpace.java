package com.hbmspace.items.armor;

import com.hbm.blocks.ModBlocks;
import com.hbm.items.IDynamicModels;
import com.hbm.items.ModItems;
import com.hbm.items.armor.ItemArmorMod;
import com.hbm.main.MainRegistry;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.items.ModItemsSpace;

public class ItemArmorModSpace extends ItemArmorMod implements IDynamicModelsSpace {
    public ItemArmorModSpace(int type, boolean helmet, boolean chestplate, boolean leggings, boolean boots, String s) {
        super(type, helmet, chestplate, leggings, boots, s);
        ModItems.ALL_ITEMS.remove(this);
        IDynamicModels.INSTANCES.remove(this);
        ModItemsSpace.ALL_ITEMS.add(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }
}
