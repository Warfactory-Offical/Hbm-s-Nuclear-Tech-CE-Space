package com.hbmspace.blocks.machine;

import com.hbm.blocks.ITooltipProvider;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.lib.ForgeDirection;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbmspace.blocks.BlockDummyableSpace;
import com.hbmspace.tileentity.machine.TileEntityMachineRocketAssembly;
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

public class MachineRocketAssembly extends BlockDummyableSpace implements ITooltipProvider {

    public MachineRocketAssembly(Material mat, String s) {
        super(mat, s);
    }

    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        if(meta >= 12) return new TileEntityMachineRocketAssembly();
        return new TileEntityProxyCombo();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityMachineRocketAssembly assembly) assembly.isBreaking = true;
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(@NotNull World world, BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return this.standardOpenBehavior(world, pos.getX(), pos.getY(), pos.getZ(), player, 0);
    }

    @Override
    public int[] getDimensions() {
        // funky behaviour, but the space checking is still done regularly
        return new int[] {0, 2, 4, 4, 4, 4};
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        x = x + dir.offsetX * o;
        y = y + dir.offsetY * o;
        z = z + dir.offsetZ * o;

        // Top
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 0, 4, 4, 4, 4}, this, dir);

        // Leggies
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 2, 4, -3, 4, -4}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 2, 4, -4, 4, -3}, this, dir);

        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 2, 4, -3, -4, 4}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 2, 4, -4, -3, 4}, this, dir);

        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 2, -3, 4, -4, 4}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 2, -4, 4, -3, 4}, this, dir);

        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 2, -3, 4, 4, -4}, this, dir);
        MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {0, 2, -4, 4, 4, -3}, this, dir);
    }

    @Override
    public int getOffset() {
        return 4;
    }

    @Override
    public int getHeightOffset() {
        return 2;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag flagIn) {
        this.addStandardInfo(list);
    }

}
