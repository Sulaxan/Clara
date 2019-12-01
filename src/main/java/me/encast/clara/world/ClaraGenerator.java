package me.encast.clara.world;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class ClaraGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);
        SimplexOctaveGenerator gen = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        gen.setScale(0.005d);
        for(int i = 0; i < 16; i++) {
            for(int k = 0; k < 16; k++) {
                int nx = x * 16 + i;
                int nz = z * 16 + k;
                double elev = (1.00 * noise1(gen, nx,  nz)
                        + 0.27 * noise1(gen, 2 * nx,  2 * nz)
                        + 0.17 * noise1(gen, 4 * nx,  4 * nz)
                        + 0.12 * noise1(gen, 8 * nx,  8 * nz)
                        + 0.06 * noise1(gen, 16 * nx, 16 * nz)
                        + 0.03 * noise1(gen, 32 * nx, 32 * nz));
                elev /= 1.00 + 0.27 + 0.17 + 0.12 + 0.06 + 0.03;
                elev = Math.pow(elev, 5.52);
                int height = (int) (elev * 15 + 50d);
                for(int j = 0; j < height; j++) {
                    chunk.setBlock(i, j, k, Material.STONE);
                }
                chunk.setBlock(i, height, k, Material.STONE);
            }
        }
        return chunk;
    }

    private double noise1(SimplexOctaveGenerator gen, double nx, double nz) {
        return gen.noise(nx, nz, 1, 1) / 2 + 0.25;
    }

    private Material getMaterial(World world, int x, int z, double e) {
        if (e < 0.035) {
            world.setBiome(x, z, Biome.OCEAN);
            return Material.WATER;
        }
        else if (e < 0.075) {
            world.setBiome(x, z, Biome.BEACH);
            return Material.SAND;
        }
        else if (e < 0.1) {
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
        else if (e < 1.0) {
            world.setBiome(x, z, Biome.DESERT);
            return Material.SAND;
        }

        world.setBiome(x, z, Biome.TAIGA);
        return Material.SNOW_BLOCK;
    }
}
