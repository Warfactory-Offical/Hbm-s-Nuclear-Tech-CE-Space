package com.hbmspace.blocks.machine;

import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineDriveProcessor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class MachineDriveProcessor extends BlockDummyableSpace {

    public MachineDriveProcessor(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityMachineDriveProcessor();
        return new TileEntityProxyCombo(true, true, false); // no need for extra atm, it's just two blocks
    }

    @Override
    public int[] getDimensions() {
        return new int[] {0, 0, 0, 0, 0, 1};
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.standardOpenBehavior(world, pos.getX(), pos.getY(), pos.getZ(), player, 0);
    }

}
