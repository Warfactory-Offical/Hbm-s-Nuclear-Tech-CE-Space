package com.hbmspace.blocks.machine.rbmk;

import com.hbm.blocks.ModBlocks;
import com.hbm.blocks.machine.rbmk.RBMKBase;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.trait.FT_Flammable;
import com.hbm.items.IDynamicModels;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.IDynamicModelsSpace;
import com.hbmspace.tileentity.machine.rbmk.TileEntityRBMKBurner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class RBMKBurner extends RBMKBase implements IDynamicModelsSpace {

    public RBMKBurner(String s, String c){
        super(s, c);
        ModBlocks.ALL_BLOCKS.remove(this);
        ModBlocksSpace.ALL_BLOCKS.add(this);
        IDynamicModels.INSTANCES.remove(this);
        IDynamicModelsSpace.INSTANCES.add(this);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {

        if(meta >= offset)
            return new TileEntityRBMKBurner();

        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(!worldIn.isRemote && !playerIn.isSneaking()) {

            ItemStack heldItem = playerIn.getHeldItem(hand);

            if(!heldItem.isEmpty() && heldItem.getItem() instanceof IItemFluidIdentifier) {
                int[] corePos = this.findCore(worldIn, pos.getX(), pos.getY(), pos.getZ());

                if(corePos == null)
                    return false;

                TileEntity te = worldIn.getTileEntity(new BlockPos(corePos[0], corePos[1], corePos[2]));

                if(!(te instanceof TileEntityRBMKBurner burner))
                    return false;

                FluidType type = ((IItemFluidIdentifier) heldItem.getItem()).getType(worldIn, corePos[0], corePos[1], corePos[2], heldItem);

                if(type.hasTrait(FT_Flammable.class) && type.getTrait(FT_Flammable.class).getHeatEnergy() > 0) {
                    burner.tank.setTankType(type);
                    burner.markDirty();
                    playerIn.sendMessage(new TextComponentString("Changed type to ").setStyle(new Style().setColor(TextFormatting.YELLOW)).appendSibling(new TextComponentTranslation("hbmfluid." + type.getName().toLowerCase())).appendSibling(new TextComponentString("!")));
                }
                return true;
            }
            return false;

        } else {
            return true;
        }
    }

    @Override
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
}
