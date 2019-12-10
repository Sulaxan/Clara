package me.encast.clara.world;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class ClaraGenerator extends ChunkGenerator {

    private static final double CHANCE_TO_LIVE = 0.45;
    private static final int STARVATION_LIMIT = 2;
    private static final int BIRTH_NUMBER = 3;
    private static final int GENERATIONS = 2;
    private static final int WORLD_SIZE = 10000; // WORLD_SIZE * WORLD_SIZE chunks
    private static final int CHUNKS = WORLD_SIZE / 16;

    private static final int CUBE_Y_START = 95;
    private static final int CUBE_Y_END = 100;

    private static final byte ALIVE = 0b1;
    private static final byte DEAD = 0b0;

    private byte[][] map;

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        if(map == null) {
            map = performGen(getMap(WORLD_SIZE, random), GENERATIONS);
        }
        ChunkData chunk = createChunkData(world);
        if(Math.abs(x) < CHUNKS && Math.abs(z) < CHUNKS) {
            if(map[x + WORLD_SIZE / 2][z + WORLD_SIZE / 2] == ALIVE) {
                for(int i = 0; i < 16; i++) {
                    for(int k = 0; k < 16; k++) {
                        for(int y = CUBE_Y_START; y <= CUBE_Y_END; y++) {
                            chunk.setBlock(i, y, k, Material.GRASS);
                        }
                    }
                }
            }
        }
        return chunk;
    }

    private Material getMaterial(World world, ChunkData data) {
        return Material.GRASS;
    }

    private byte[][] getMap(int worldSize, Random random) {
        byte[][] world = new byte[worldSize][worldSize];
        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world[i].length; i++) {
                world[i][j] = random.nextDouble() < CHANCE_TO_LIVE ? ALIVE : DEAD;
            }
        }
        return world;
    }

    private byte[][] performGen(byte[][] map, int generations) {
        if(generations == 0)
            return map;
        byte[][] newMap = new byte[map.length][map.length];
        for(int x = 0; x < map.length; x++) {
            for(int z = 0; z < map[x].length; z++) {
                int neighbours = getNeighbourCount(map, x, z);
                if(map[x][z] == ALIVE) {
                    newMap[x][z] = neighbours < STARVATION_LIMIT ? DEAD : ALIVE;
                } else {
                    newMap[x][z] = neighbours > BIRTH_NUMBER ? ALIVE : DEAD;
                }
            }
        }
        return performGen(newMap, generations - 1);
    }

    private int getNeighbourCount(byte[][] map, int x, int z) {
        int count = 0;
        int dx, dz;
        for(int i = -1; i <= 1; i++) {
            for(int k = -1; k <= 1; k++) {
                if(i == 0 && k == 0)
                    continue;
                dx = x + i;
                dz = z + i;
                if(dx < 0 || dz < 0 || dx >= map.length || dz >= map.length)
                    continue;

                if(map[dx][dz] == ALIVE)
                    count++;
            }
        }
        return count;
    }
}
