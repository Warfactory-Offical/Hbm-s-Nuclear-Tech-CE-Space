package com.hbmspace.dim.orbit;

import com.hbmspace.config.SpaceConfig;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;
import java.util.Random;

public class ChunkProviderOrbit implements IChunkGenerator {

	protected World worldObj;
	private final Random rand;

	public ChunkProviderOrbit(World world) {
		this.worldObj = world;
		this.rand = new Random(world.getSeed());
	}

	@Override
	public Chunk generateChunk(int x, int z) {
		Chunk chunk = new Chunk(worldObj, new ChunkPrimer(), x, z);
			byte[] biomes = chunk.getBiomeArray();
			for(int k = 0; k < biomes.length; ++k) {
				biomes[k] = (byte) SpaceConfig.orbitBiome;
			}

		chunk.generateSkylightMap();
		return chunk;
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	@Override
	public void populate(int x, int z) {
		BlockFalling.fallInstantly = true;
		this.rand.setSeed(this.worldObj.getSeed());
		long i1 = this.rand.nextLong() / 2L * 2L + 1L;
		long j1 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long) x * i1 + (long) z * j1 ^ this.worldObj.getSeed());
		ForgeEventFactory.onChunkPopulate(true, this, this.worldObj, this.rand, x, z, false);
		ForgeEventFactory.onChunkPopulate(false, this, this.worldObj, this.rand, x, z, false);
		BlockFalling.fallInstantly = false;
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z){return false;}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos){return false;}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the given
	 * location.
	 */
	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return null;
	}

	/**
	 * I have no fucking clue, just return null
	 */
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {

	}

}
