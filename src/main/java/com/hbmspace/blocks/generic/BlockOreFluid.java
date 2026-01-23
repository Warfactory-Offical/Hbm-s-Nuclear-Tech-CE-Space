package com.hbmspace.blocks.generic;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbmspace.config.WorldConfigSpace;
import com.hbmspace.dim.SolarSystem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Random;
// TODO finally add tekto, retard (saying that to myself)
public class BlockOreFluid extends BlockOre {

    private final Block empty;
    private final ReserveType type;

    private static final HashMap<Block, Block> emptyToFull = new HashMap<>();

    public enum ReserveType {
        OIL,
        GAS,
        BRINE,
    }

    public BlockOreFluid(String name, Block empty, ReserveType type) {
        super(name, null, 3);
        this.empty = empty;
        this.type = type;

        if (empty != null) {
            emptyToFull.put(empty, this);
        }
    }

    public String getUnlocalizedReserveType() {
        return switch (type) {
            case GAS -> "_gas";
            case BRINE -> "_brine";
            default -> "";
        };
    }

    public FluidType getPrimaryFluid(int meta) {
        switch (type) {
            case OIL:
                if (meta == SolarSystem.Body.LAYTHE.ordinal()) return Fluids.OIL_DS;
                //if (meta == SolarSystem.Body.TEKTO.ordinal()) return com.hbmspace.inventory.fluid.Fluids.TCRUDE;
                return Fluids.OIL;
            case GAS:
                return Fluids.GAS;
            case BRINE:
                return com.hbmspace.inventory.fluid.Fluids.BRINE;
            default:
                return Fluids.NONE;
        }
    }

    public FluidType getSecondaryFluid(int meta) {
        return switch (type) {
            case OIL -> //if (meta == SolarSystem.Body.TEKTO.ordinal()) yield com.hbmspace.inventory.fluid.Fluids.HGAS;
                    Fluids.GAS;
            case GAS -> Fluids.PETROLEUM;
            default -> Fluids.NONE;
        };
    }

    public int getBlockFluidAmount(int meta) {
        return switch (type) {
            case OIL -> 250;
            case GAS -> 100;
            default -> 0;
        };
    }

    public int getPrimaryFluidAmount(int meta) {
        if (empty == null) {
            //if (meta == SolarSystem.Body.TEKTO.ordinal()) return WorldConfigSpace.tektoBedrockOilPerDeposit;
            return WorldConfigSpace.bedrockOilPerDeposit;
        }

        if (meta == SolarSystem.Body.DUNA.ordinal()) return WorldConfigSpace.dunaOilPerDeposit;
        if (meta == SolarSystem.Body.LAYTHE.ordinal()) return WorldConfigSpace.laytheOilPerDeposit;
        if (meta == SolarSystem.Body.EVE.ordinal()) return WorldConfigSpace.eveGasPerDeposit;
        if (meta == SolarSystem.Body.MUN.ordinal()) return WorldConfigSpace.munBrinePerDeposit;
        if (meta == SolarSystem.Body.MINMUS.ordinal()) return WorldConfigSpace.minmusBrinePerDeposit;
        if (meta == SolarSystem.Body.IKE.ordinal()) return WorldConfigSpace.ikeBrinePerDeposit;
        //if (meta == SolarSystem.Body.TEKTO.ordinal()) return WorldConfigSpace.tektoOilPerDeposit;
        return WorldConfigSpace.earthOilPerDeposit;
    }

    public int getSecondaryFluidAmount(int meta) {
        Random rand = new Random();

        if (empty == null) {
            /*if (meta == SolarSystem.Body.TEKTO.ordinal())
                return WorldConfigSpace.tektoBedrockGasPerDepositMin + rand.nextInt(WorldConfigSpace.tektoBedrockGasPerDepositMax - WorldConfigSpace.tektoBedrockGasPerDepositMin);*/
            return WorldConfigSpace.bedrockGasPerDepositMin + rand.nextInt(WorldConfigSpace.bedrockGasPerDepositMax - WorldConfigSpace.bedrockGasPerDepositMin);
        }

        if (meta == SolarSystem.Body.DUNA.ordinal())
            return WorldConfigSpace.dunaGasPerDepositMin + rand.nextInt(WorldConfigSpace.dunaGasPerDepositMax - WorldConfigSpace.dunaGasPerDepositMin);
        if (meta == SolarSystem.Body.LAYTHE.ordinal())
            return WorldConfigSpace.laytheGasPerDepositMin + rand.nextInt(WorldConfigSpace.laytheGasPerDepositMax - WorldConfigSpace.laytheGasPerDepositMin);
        if (meta == SolarSystem.Body.EVE.ordinal())
            return WorldConfigSpace.evePetPerDepositMin + rand.nextInt(WorldConfigSpace.evePetPerDepositMax - WorldConfigSpace.evePetPerDepositMin);
        /*if (meta == SolarSystem.Body.TEKTO.ordinal())
            return WorldConfigSpace.tektoGasPerDepositMin + rand.nextInt(WorldConfigSpace.tektoGasPerDepositMax - WorldConfigSpace.tektoGasPerDepositMin);*/
        return WorldConfigSpace.earthGasPerDepositMin + rand.nextInt(WorldConfigSpace.earthGasPerDepositMax - WorldConfigSpace.earthGasPerDepositMin);
    }

    private double getDrainChance(int meta) {
        if (empty == null) return 0;
        if (meta == SolarSystem.Body.DUNA.ordinal()) return WorldConfigSpace.dunaOilDrainChance;
        if (meta == SolarSystem.Body.LAYTHE.ordinal()) return WorldConfigSpace.laytheOilDrainChance;
        if (meta == SolarSystem.Body.EVE.ordinal()) return WorldConfigSpace.eveGasDrainChance;
        if (meta == SolarSystem.Body.MUN.ordinal()) return WorldConfigSpace.munBrineDrainChance;
        if (meta == SolarSystem.Body.MINMUS.ordinal()) return WorldConfigSpace.minmusBrineDrainChance;
        if (meta == SolarSystem.Body.IKE.ordinal()) return WorldConfigSpace.ikeBrineDrainChance;
        //if (meta == SolarSystem.Body.TEKTO.ordinal()) return WorldConfigSpace.tektoOilDrainChance;
        return WorldConfigSpace.earthOilDrainChance;
    }

    public void drain(World world, BlockPos pos, int meta, double chanceMultiplier) {
        if (empty == null) return;

        if (world.rand.nextDouble() < getDrainChance(meta) * chanceMultiplier) {
            world.setBlockState(pos, empty.getDefaultState().withProperty(META, meta), 3);
        }
    }

    public boolean requiresFracking() {
        return empty == null;
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        if (empty == null) return;

        BlockPos downPos = pos.down();
        IBlockState downState = world.getBlockState(downPos);

        if (downState.getBlock() == empty) {
            int currentMeta = state.getValue(META);

            world.setBlockState(pos, empty.getDefaultState().withProperty(META, currentMeta), 3);
            world.setBlockState(downPos, this.getDefaultState().withProperty(META, currentMeta), 3);
        }
    }

    public static Block getFullBlock(Block block) {
        return emptyToFull.get(block);
    }
}
