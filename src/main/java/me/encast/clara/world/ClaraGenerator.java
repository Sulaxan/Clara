package me.encast.clara.world;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.encast.clara.Clara;
import me.encast.clara.util.map.chunk.ClaraChunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class ClaraGenerator extends ChunkGenerator {

    private static final double CHANCE_TO_LIVE = 0.45;
    private static final int STARVATION_LIMIT = 2;
    private static final int BIRTH_NUMBER = 3;
    private static final int GENERATIONS = 2;
    private static final int WORLD_SIZE = 1000; //10000; // WORLD_SIZE * WORLD_SIZE chunks
    private static final int CHUNKS = WORLD_SIZE / 16;

    private static final int[][] CIRCLE = new int[16][16];
    private static MaterialData[] ISLAND_MATERIALS;
    private static final int Y_START = 91;
    private static final int Y_END = 100;

    private static final byte ALIVE = 0b1;
    private static final byte DEAD = 0b0;

    static {
        ISLAND_MATERIALS = new MaterialData[] {
               new MaterialData(Material.STONE),
               new MaterialData(Material.STONE, (byte) 5),
               new MaterialData(Material.STONE, (byte) 6)
        };
        CIRCLE[6][2] = 1;
        CIRCLE[7][2] = 1;
        CIRCLE[8][2] = 1;
        CIRCLE[9][2] = 1;
        CIRCLE[10][3] = 1;
        CIRCLE[11][3] = 1;
        CIRCLE[12][4] = 1;
        CIRCLE[12][5] = 1;
        CIRCLE[13][6] = 1;
        CIRCLE[13][7] = 1;
        CIRCLE[13][8] = 1;
        CIRCLE[13][9] = 1;
        CIRCLE[12][10] = 1;
        CIRCLE[12][11] = 1;
        CIRCLE[11][12] = 1;
        CIRCLE[10][12] = 1;
        CIRCLE[9][13] = 1;
        CIRCLE[8][13] = 1;
        CIRCLE[7][13] = 1;
        CIRCLE[6][13] = 1;
        CIRCLE[5][12] = 1;
        CIRCLE[4][12] = 1;
        CIRCLE[3][11] = 1;
        CIRCLE[3][10] = 1;
        CIRCLE[2][9] = 1;
        CIRCLE[2][8] = 1;
        CIRCLE[2][7] = 1;
        CIRCLE[2][6] = 1;
        CIRCLE[3][5] = 1;
        CIRCLE[3][4] = 1;
        CIRCLE[4][3] = 1;
        CIRCLE[5][3] = 1;


        // Overhang
        CIRCLE[6][1] = 2;
        CIRCLE[7][1] = 2;
        CIRCLE[8][1] = 2;
        CIRCLE[9][1] = 2;
        CIRCLE[10][2] = 2;
        CIRCLE[11][2] = 2;
        CIRCLE[12][3] = 2;
        CIRCLE[13][4] = 2;
        CIRCLE[13][5] = 2;
        CIRCLE[14][6] = 2;
        CIRCLE[14][7] = 2;
        CIRCLE[14][8] = 2;
        CIRCLE[14][9] = 2;
        CIRCLE[13][10] = 2;
        CIRCLE[13][11] = 2;
        CIRCLE[12][12] = 2;
        CIRCLE[11][13] = 2;
        CIRCLE[10][13] = 2;
        CIRCLE[9][14] = 2;
        CIRCLE[7][14] = 2;
        CIRCLE[8][14] = 2;
        CIRCLE[6][14] = 2;
        CIRCLE[5][13] = 2;
        CIRCLE[4][13] = 2;
        CIRCLE[3][12] = 2;
        CIRCLE[2][11] = 2;
        CIRCLE[2][10] = 2;
        CIRCLE[1][9] = 2;
        CIRCLE[1][8] = 2;
        CIRCLE[1][7] = 2;
        CIRCLE[1][6] = 2;
        CIRCLE[2][5] = 2;
        CIRCLE[2][4] = 2;
        CIRCLE[3][3] = 2;
        CIRCLE[4][2] = 2;
        CIRCLE[5][2] = 2;
    }

    private byte[][] map;

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        if(map == null) {
            map = performGen(getMap(WORLD_SIZE, random), GENERATIONS);
        }
        ChunkData chunk = createChunkData(world);
        if(Math.abs(x) < CHUNKS && Math.abs(z) < CHUNKS) {
            if(map[x + CHUNKS / 2][z + CHUNKS / 2] == ALIVE) {
                generateIsland(chunk, random, biome);
                setData(world, chunk, random, biome, x, z);
            }
        }
        return chunk;
    }

    public void generateIsland(ChunkData data, Random random, BiomeGrid biome) {
        Map<Integer, Vertex> vertices = Maps.newHashMap();
        Vertex temp;
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                vertices.put(x * 50 + z, temp = new Vertex(x, z, false));
                int val = Clara.MAP.getIslandAttributes()[x][z];
                if(val == 1) {
                    for(int y = Clara.MAP.getIslandStartY(); y <= Clara.MAP.getIslandEndY(); y++) {
                        if(y != Clara.MAP.getIslandEndY()) {
                            data.setBlock(x, y, z, Clara.MAP.getMaterials()[random.nextInt(Clara.MAP.getMaterials().length)].toBukkitData());
                        } else {
                            // Change depending on the biome
                            data.setBlock(x, y, z, Material.GRASS);
                            temp.setVisited(true);
                        }
                    }
                } else if(val == 2) {
                    if(random.nextDouble() < 0.25)
                        continue;
                    for(int y = Y_END - 2 - random.nextInt(5); y <= Y_END - 1; y++) {
                        if(y != (Y_END - 1)) {
                            data.setBlock(x, y, z, Clara.MAP.getMaterials()[random.nextInt(Clara.MAP.getMaterials().length)].toBukkitData());
                        } else {
                            // Change depending on the biome
                            data.setBlock(x, y, z, Material.GRASS);
                        }
                    }
                }
            }
        }

        // Breadth-First Search to fill middle grass (flood fill algorithm?)
        Queue<Vertex> checkLocs = new LinkedList<>();
        checkLocs.add(vertices.get(7 * 50 + 7));

        int dx, dz;
        while(!checkLocs.isEmpty()) {
            Vertex loc = checkLocs.remove();
            if(loc.isVisited())
                continue;
            for(int i = -1; i <= 1; i++) {
                for(int k = -1; k <= 1; k++) {
                    if((i == 0 && k == 0) || (i != 0 && k != 0))
                        continue;
                    dx = loc.getX() + i;
                    dz = loc.getZ() + k;
                    if(dx < 0 || dz < 0 || dx > 15 || dz > 15)
                        continue;
                    Vertex v = vertices.getOrDefault(dx * 50 + dz, null);
                    if(v != null && !v.isVisited())
                        checkLocs.offer(v);
                }
            }
            loc.setVisited(true);
            data.setBlock(loc.getX(), 100, loc.getZ(), Material.GRASS);
        }
    }

    public ClaraChunk setData(World world, ChunkData data, Random random, BiomeGrid grid, int x, int z) {
        ClaraChunk chunk = new ClaraChunk(x, z);
        chunk.setDungeonGate(random.nextDouble() < Clara.MAP.getDungeonSpawnRate());
        chunk.setBiomeId("default_biome");
        Clara.getInstance().getChunkClusterManager().updateCluster(world, chunk);

        return chunk;
    }

    private byte[][] getMap(int worldSize, Random random) {
        byte[][] world = new byte[worldSize][worldSize];
        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world[i].length; j++) {
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

    @AllArgsConstructor
    @Getter
    @Setter
    private class Vertex {

        private int x, z;
        private boolean visited;
    }
}
