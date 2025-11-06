package com.hbmspace.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineStardar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MachineStardar extends BlockDummyableSpace implements ITooltipProvider {

    public MachineStardar(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityMachineStardar();
        if(meta >= extra) return new TileEntityProxyCombo(false, false, true);
        return null;
    }

    @Override
    public int[] getDimensions() {
        return new int[] {0, 3, 2, 2, 2, 2};
    }

    @Override
    public int getOffset() {
        return 2;
    }

    @Override
    public int getHeightOffset() {
        return 3;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        // Main body
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 0, 2, 2, 2, 2}, this, dir);

        // Legs
        for(int ox = -2; ox <= 2; ox += 2) {
            for(int oz = -2; oz <= 2; oz += 2) {
                if(ox == 0 && oz == 0) continue;
                MultiblockHandlerXR.fillSpace(world, x + ox, y, z + oz, new int[] {0, 3, 0, 0, 0, 0}, this, dir);
            }
        }

        // Rack
        BlockDummyable.safeRem = true;
        world.setBlockState(new BlockPos(x - rot.offsetX + dir.offsetX * 2, y - 2, z - rot.offsetZ + dir.offsetZ * 2), ModBlocksSpace.machine_stardar.getDefaultState().withProperty(BlockDummyable.META, rot.ordinal()));
        world.setBlockState(new BlockPos(x - rot.offsetX + dir.offsetX * 2, y - 3, z - rot.offsetZ + dir.offsetZ * 2), ModBlocksSpace.machine_stardar.getDefaultState().withProperty(BlockDummyable.META, rot.ordinal()));
        BlockDummyable.safeRem = false;
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.standardOpenBehavior(world, pos.getX(), pos.getY(), pos.getZ(), player, 0);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }
}
