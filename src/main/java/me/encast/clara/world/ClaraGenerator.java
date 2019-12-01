package me.encast.clara.world;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import java.util.Random;

public class ClaraGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);
        PerlinOctaveGenerator gen = new PerlinOctaveGenerator(world, 8);
        for(int i = 0; i < 16; i++) {
            for(int k = 0; k < 16; k++) {
                double elev = gen.noise(x * 16 + i, z * 16 + k, 1, 1) +
                        gen.noise(x * 16 * 2 + i, z * 16 * 2 + k, 0.5, 1) +
                        gen.noise(x * 16 * 4 + i, z * 16 * 4 + k, 0.25, 1);
                elev = Math.pow(elev, 1.85);
                int y = (int) elev * 1000;
                for(int j = 0; j < y; j++) {
                    chunk.setBlock(i, y, j, Material.STONE);
                }
                chunk.setBlock(x, y, z, getMaterial(world, x * 16 + i, z * 16 + i, elev));
            }
        }
        return chunk;
    }

    private Material getMaterial(World world, int x, int z, double e) {
        if (e < 0.1) {
            world.setBiome(x, z, Biome.OCEAN);
            return Material.WOOL;
        }
        else if (e < 0.2) {
            world.setBiome(x, z, Biome.BEACH);
            return Material.SAND;
        }
        else if (e < 0.3) {
            world.setBiome(x, z, Biome.FOREST);
            return Material.GRASS;
        }
        else if (e < 0.5) {
            world.setBiome(x, z, Biome.JUNGLE);
            return Material.GRASS;
        }
        else if (e < 0.7) {
            world.setBiome(x, z, Biome.SAVANNA);
            return Material.GRASS;
        }
        else if (e < 0.9) {
            world.setBiome(x, z, Biome.DESERT);
            return Material.SAND;
        }

        world.setBiome(x, z, Biome.TAIGA);
        return Material.SNOW;
    }
}
