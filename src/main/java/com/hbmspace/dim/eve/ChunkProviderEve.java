package com.hbmspace.dim.eve;

import com.hbm.blocks.ModBlocks;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.dim.ChunkProviderCelestial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


public class ChunkProviderEve extends ChunkProviderCelestial {
	
	public ChunkProviderEve(World world, long seed, boolean hasMapFeatures) {
		super(world, seed, hasMapFeatures);
		reclamp = false;
		stoneBlock = ModBlocksSpace.eve_rock;
		seaBlock = ModBlocks.mercury_block;
	}

	@Override
	public ChunkPrimer getChunkPrimer(int x, int z) {

		// how many times do I gotta say BEEEEG
		return super.getChunkPrimer(x, z);
	}

	@Override
	public boolean generateStructures(@NotNull Chunk chunkIn, int x, int z){return false;}
	@Override
	@Nullable
	public BlockPos getNearestStructurePos(@NotNull World worldIn, @NotNull String structureName, @NotNull BlockPos position, boolean findUnexplored){return null;}
	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z){}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos){return false;}
}