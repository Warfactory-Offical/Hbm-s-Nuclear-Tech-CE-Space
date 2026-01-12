package com.hbmspace.dim;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BiomeGenBaseCelestial extends Biome {

    protected ArrayList<SpawnListEntry> creatures = new ArrayList<>();
    protected ArrayList<SpawnListEntry> monsters = new ArrayList<>();
    protected ArrayList<SpawnListEntry> waterCreatures = new ArrayList<>();
    protected ArrayList<SpawnListEntry> caveCreatures = new ArrayList<>();

    public BiomeGenBaseCelestial(BiomeProperties properties) {
        super(properties);
    }

    // Returns a copy of the lists to prevent them being modified
    @SuppressWarnings("rawtypes")
    @Override
    public @NotNull List<SpawnListEntry> getSpawnableList(EnumCreatureType type) {
        return switch (type) {
            case MONSTER -> (List) monsters.clone();
            case CREATURE -> (List) creatures.clone();
            case WATER_CREATURE -> (List) waterCreatures.clone();
            case AMBIENT -> (List) caveCreatures.clone();
            default -> Collections.emptyList();
        };
    }
    
}
