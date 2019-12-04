package me.encast.clara.world;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class ClaraGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);
        SimplexOctaveGenerator gen = new SimplexOctaveGenerator(new Random(world.getSeed()), 1);
        gen.setScale(0.005d);
        // Creating a 16x16 island, everything else is water
        if((x < -16 || x > 16) || (z < -16 || z > 16)) {
            for(int i = 0; i < 16; i++) {
                for(int k = 0; k < 16; k++) {
                    int nx = x * 16 + i;
                    int nz = z * 16 + k;
                    double noise =  1.00 * gen.noise(nx, nz, 16, 0.25, true) +
                                    0.28 * gen.noise(4 * nx, 4 * nz, 12, 0.2, true);
                    noise = (noise + 1) / 2; // further normalizing between [0, infinity]
                    noise = Math.abs(noise);
                    int y = (int) (noise * 100d + 20d);

                    // Fill with water
                }
            }
        }
        for(int i = 0; i < 16; i++) {
            for(int k = 0; k < 16; k++) {
                int nx = x * 16 + i;
                int nz = z * 16 + k;
                double noise =  1.00 * gen.noise(nx, nz, 16, 0.72, true) +
//                                0.52 * gen.noise(4 * nx, 4 * nz, 1, 1, true) +
                                0.28 * gen.noise(4 * nx, 4 * nz, 12, 0.2, true) +
                                0.07 * gen.noise(16 * nx, 16 * nz, 12, 0.1);
//                                0.03 * gen.noise(32 * nx, 32 * nz, 1, 1, true);
                noise = (noise + 1) / 2; // further normalizing between [0, infinity]
                noise = Math.abs(noise);
                int y = (int) (noise * 100d + 20d);


                for(int j = 1; j < y; j++) {
                    chunk.setBlock(i, j, k, Material.STONE);
                }
                chunk.setBlock(i, 0, k, Material.BEDROCK);
                if(y <= world.getSeaLevel()) {
                    for(int j = y; j <= world.getSeaLevel(); j++) {
                        chunk.setBlock(i, j, k, Material.WATER);
                    }
                    chunk.setBlock(i, y, k, Material.SAND);
                } else if(y <= world.getSeaLevel() + 5) {
                    chunk.setBlock(i, y, k, Material.SAND);
                } else if(y >= 176) {
                    if(y >= 200) {
                        chunk.setBlock(i, y, k, Material.SNOW);
                    } else {
                        chunk.setBlock(i, y, k, Material.STONE);
                    }
                } else {
                    chunk.setBlock(i, y, k, Material.GRASS);
                }
            }
        }
        return chunk;
    }

    private double noise1(SimplexOctaveGenerator gen, double nx, double nz) {
        return gen.noise(nx, nz, 1, 1) / 2 + 0.25;
    }

    private Material getMaterial(World world, double noise) {
        return Material.GRASS;
    }
}
