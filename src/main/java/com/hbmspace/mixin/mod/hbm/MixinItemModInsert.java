package com.hbmspace.mixin.mod.hbm;

import com.hbm.items.armor.ItemModInsert;
import com.hbmspace.accessors.IHaveCorrosionProtAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ItemModInsert.class, remap = false)
public class MixinItemModInsert implements IHaveCorrosionProtAccessor {

    @Unique
    private boolean corrosionProtection;

    @Unique
    @Override
    public ItemModInsert withCorrosionProtection() {
        this.corrosionProtection = true;
        return (ItemModInsert) (Object) this;
    }

    @Unique
    @Override
    public boolean getHaveCorProt() {
        return this.corrosionProtection;
    }

}
