package me.encast.clara.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.encast.clara.Clara;
import me.encast.clara.util.map.ClaraBiome;
import me.encast.clara.util.map.ClaraMapResource;
import me.encast.clara.util.map.chunk.ClaraChunk;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

import java.util.*;

public class ClaraGenerator extends ChunkGenerator {

    private static final int CHUNKS = Clara.MAP.getWorldSize() / 16;

    private static final byte ALIVE = 0b1;
    private static final byte DEAD = 0b0;

    private byte[][] map;
    private int[][] biomeMap;

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        if(map == null) {
            map = performGen(getMap(Clara.MAP.getWorldSize(), random), Clara.MAP.getGenerations());
            biomeMap = generateBiomes(map, random);
        }

        ChunkData chunk = createChunkData(world);
        int chunkX = x + CHUNKS - 1;
        int chunkZ = z + CHUNKS - 1;
        if(chunkX >= 0 && chunkZ >= 0) {
            if(map[chunkX][chunkZ] == ALIVE) {
                setData(world, chunk, random, biome, x, z);
                generateIsland(chunk, random, biome, x, z);
            }
        }
        return chunk;
    }

    public void generateIsland(ChunkData data, Random random, BiomeGrid biome, int chunkX, int chunkZ) {
        ClaraBiome claraBiome = Clara.MAP.getBiome(biomeMap[chunkX + Clara.MAP.getWorldSize() / 2][chunkZ + Clara.MAP.getWorldSize() / 2]);
        MaterialData top = claraBiome.getMaterials().get(0).toBukkitData(); // temp
        Map<Integer, Vertex> vertices = Maps.newHashMap();
        Vertex temp;
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                vertices.put(x * 50 + z, temp = new Vertex(x, z, false, false));
                int val = Clara.MAP.getIslandAttributes()[x][z];
                if(val == 1) {
                    for(int y = Clara.MAP.getIslandStartY(); y <= Clara.MAP.getIslandEndY(); y++) {
                        if(y != Clara.MAP.getIslandEndY()) {
                            data.setBlock(x, y, z, Clara.MAP.getMaterials()[random.nextInt(Clara.MAP.getMaterials().length)].toBukkitData());
                        } else {
                            // Change depending on the biome
                            data.setBlock(x, y, z, top);
                            temp.setVisited(true);
                        }
                    }
                } else if(val == 2) {
                    if(random.nextDouble() < 0.25)
                        continue;
                    for(int y = Clara.MAP.getIslandEndY() - 2 - random.nextInt(5); y <= Clara.MAP.getIslandEndY() - 1; y++) {
                        if(y != (Clara.MAP.getIslandEndY() - 1)) {
                            data.setBlock(x, y, z, Clara.MAP.getMaterials()[random.nextInt(Clara.MAP.getMaterials().length)].toBukkitData());
                        } else {
                            // Change depending on the biome
                            data.setBlock(x, y, z, top);
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
            data.setBlock(loc.getX(), Clara.MAP.getIslandEndY(), loc.getZ(), top);
        }
    }

    public ClaraChunk setData(World world, ChunkData data, Random random, BiomeGrid grid, int x, int z) {
        ClaraChunk chunk = new ClaraChunk(x, z);
        chunk.setDungeonGate(random.nextDouble() < Clara.MAP.getDungeonSpawnRate());
        ClaraBiome biome = Clara.MAP.getBiome(biomeMap[x + Clara.MAP.getWorldSize() / 2][z + Clara.MAP.getWorldSize() / 2]);
        if(biome == null)
            biome = ClaraBiome.DEFAULT_BIOME;
        chunk.setBiomeId(biome.getId());
        for(int i = 0; i < 16; i++) {
            for(int k = 0; k < 16; k++) {
                grid.setBiome(i, k, biome.getMinecraftBiome());
            }
        }
        Clara.getInstance().getChunkClusterManager().updateCluster(world, chunk);

        return chunk;
    }

    private byte[][] getMap(int worldSize, Random random) {
        byte[][] world = new byte[worldSize][worldSize];
        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world[i].length; j++) {
                world[i][j] = random.nextDouble() < Clara.MAP.getChanceToLive() ? ALIVE : DEAD;
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
                    newMap[x][z] = neighbours < Clara.MAP.getStarvationLimit() ? DEAD : ALIVE;
                } else {
                    newMap[x][z] = neighbours > Clara.MAP.getBirthCount() ? ALIVE : DEAD;
                }
            }
        }
        return performGen(newMap, generations - 1);
    }

    private int[][] generateBiomes(byte[][] map, Random random) {
        ClaraMapResource claraMap = Clara.MAP;
        List<ClaraBiome> biomeList = Lists.newArrayList(claraMap.getBiomeCache().values());
        int[][] biomes = new int[claraMap.getWorldSize()][claraMap.getWorldSize()];
        Map<Integer, Vertex> vertices = Maps.newHashMap();
        for(int x = 0; x < claraMap.getWorldSize(); x++) {
            for(int z = 0; z < claraMap.getWorldSize(); z++) {
                vertices.put(x * 500 + z, new Vertex(x, z, false, false));
            }
        }

        int x = random.nextInt(claraMap.getWorldSize());
        int z = random.nextInt(claraMap.getWorldSize());
        ClaraBiome current;
        int size;
        Stack<Vertex> toCheck = new Stack<>();
        toCheck.push(vertices.get(x * 500 + z));
        while (!toCheck.isEmpty()) {
            size = 0;
            current = getRandomBiome(biomeList, random); // properly transition later
            Queue<Vertex> queue = new LinkedList<>();
            queue.offer(toCheck.pop());
            boolean run = true;
            while(!queue.isEmpty()) {
                Vertex vertex = queue.remove();
                if(vertex == null || vertex.isVisited())
                    continue;
                vertex.setVisited(true);
                for(int i = -1; i <= 1; i++) {
                    for(int k = -1; k <= 1; k++) {
                        if((i == 0 && k == 0) || (i != 0 && k != 0))
                            continue;
                        int dx = vertex.getX() + i;
                        int dz = vertex.getZ() + k;
                        Vertex v = vertices.get(dx * 500 + dz);
                        if(v != null && !v.isVisited() && !v.isEnqueued()) {
                            // 45% chance to continue expanding if min span has been exceeded
                            if(size < current.getMinSpan() || random.nextDouble() <= 0.45) {
                                biomes[dx][dz] = current.getId();
                                queue.offer(v);
//                                size++;
                            } else {
                                run = false;
                                while (!queue.isEmpty()) {
                                    toCheck.push(queue.remove());
                                    toCheck.peek().setEnqueued(false);
                                }
                                break;
                            }
                        }
                    }
                    if(!run)
                        break;
                }
                if(map[vertex.getX()][vertex.getZ()] == ALIVE)
                    size++;
                biomes[vertex.getX()][vertex.getZ()] = current.getId();
                if(!run)
                    break;
            }
        }

        return biomes;
    }

    private boolean isAllVisited(Map<Integer, Vertex> vertices) {
        for(Vertex v : vertices.values()) {
            if(!v.isVisited())
                return false;
        }
        return true;
    }

    private ClaraBiome getRandomBiome(List<ClaraBiome> biomes, Random random) {
        return biomes.get(random.nextInt(biomes.size()));
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
        private boolean visited, enqueued;
    }
}
