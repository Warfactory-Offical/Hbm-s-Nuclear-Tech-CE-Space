package com.hbmspace.mixin.mod.hbm;

import com.hbm.hazard.HazardRegistry;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.material.MaterialShapes;
import com.hbmspace.inventory.IDictFrameAddon;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = OreDictManager.DictFrame.class, remap = false)
public abstract class MixinDictFrame implements IDictFrameAddon {

    @Shadow
    float hazMult;
    @Shadow
    public abstract void registerStack(String tag, ItemStack stack);
    @Unique
    public OreDictManager.DictFrame makeObject(MaterialShapes shape, int meta, Object... objects) {

        String tag = shape.name();
        for(Object o : objects) {
            if(o instanceof Item)		registerStack(tag, new ItemStack((Item) o, 1, meta));
            if(o instanceof Block)		registerStack(tag, new ItemStack((Block) o, 1, meta));
            if(o instanceof ItemStack)	registerStack(tag, (ItemStack) o);
        }

        return (OreDictManager.DictFrame) (Object) this;
    }

    @Override
    public OreDictManager.DictFrame oreAll(Object... ore) { // Ignores metadata
        this.hazMult = HazardRegistry.ore;
        return this.makeObject(MaterialShapes.ORE, OreDictionary.WILDCARD_VALUE, ore);
    }
}
