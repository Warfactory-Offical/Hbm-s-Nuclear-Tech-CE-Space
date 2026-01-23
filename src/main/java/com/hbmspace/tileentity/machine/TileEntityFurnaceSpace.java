package com.hbmspace.tileentity.machine;

import com.hbmspace.blocks.machine.BlockFurnaceSpace;
import com.hbmspace.interfaces.AutoRegister;
import com.hbmspace.tileentity.TESpaceUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
@AutoRegister
public class TileEntityFurnaceSpace extends TileEntityFurnace implements ITickable {

    @Override
    public void update() {
        boolean isBurning = this.isBurning();
        boolean dirty = false;

        int burnTime = this.getField(0);
        if (burnTime > 0) {
            this.setField(0, burnTime - 1);
        }

        if (!this.world.isRemote) {
            ItemStack input = this.getStackInSlot(0);
            ItemStack fuel = this.getStackInSlot(1);
            ItemStack output = this.getStackInSlot(2);

            if (this.getField(0) != 0 || !input.isEmpty() && !fuel.isEmpty()) {
                if (this.getField(0) == 0 && this.canSmeltCustom(input, output)) {
                    int itemBurnTime = getItemBurnTime(fuel);

                    if (itemBurnTime > 0 && fuel.getItem() != Items.LAVA_BUCKET && !TESpaceUtil.breatheAir(this.world, this.pos, 0)) {
                        itemBurnTime = 0;
                    }

                    this.setField(0, itemBurnTime);
                    this.setField(1, itemBurnTime);

                    if (this.isBurning()) {
                        dirty = true;
                        if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);

                            if (fuel.isEmpty()) {
                                ItemStack container = item.getContainerItem(fuel);
                                this.setInventorySlotContents(1, container);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmeltCustom(input, output)) {
                    int cookTime = this.getField(2);
                    this.setField(2, cookTime + 1);

                    if (this.getField(2) == 200) { // Standard total cook time
                        this.setField(2, 0);
                        this.smeltItemCustom(input, output);
                        dirty = true;
                    }
                } else {
                    this.setField(2, 0);
                }
            } else if (!this.isBurning() && this.getField(2) > 0) {
                this.setField(2, MathHelper.clamp(this.getField(2) - 2, 0, 200));
            }

            if (isBurning != this.isBurning()) {
                dirty = true;
                BlockFurnaceSpace.updateFurnaceBlockState(this.isBurning(), this.world, this.pos);
            }
        }

        if (dirty) {
            this.markDirty();
        }
    }

    private boolean canSmeltCustom(ItemStack input, ItemStack output) {
        if (input.isEmpty()) {
            return false;
        } else {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);

            if (result.isEmpty()) {
                return false;
            } else {
                if (output.isEmpty()) return true;
                if (!output.isItemEqual(result)) return false;
                int res = output.getCount() + result.getCount();
                return res <= getInventoryStackLimit() && res <= output.getMaxStackSize();
            }
        }
    }

    private void smeltItemCustom(ItemStack input, ItemStack output) {
        if (this.canSmeltCustom(input, output)) {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);

            if (output.isEmpty()) {
                this.setInventorySlotContents(2, result.copy());
            } else if (output.getItem() == result.getItem()) {
                output.grow(result.getCount());
            }

            if (input.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && input.getMetadata() == 1 && !this.getStackInSlot(1).isEmpty() && this.getStackInSlot(1).getItem() == Items.BUCKET) {
                this.setInventorySlotContents(1, new ItemStack(Items.WATER_BUCKET));
            }

            input.shrink(1);
        }
    }
}
