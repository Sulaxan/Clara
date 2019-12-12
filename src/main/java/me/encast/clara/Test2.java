package me.encast.clara;

import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.Executors;

public class Test2 {

    private static final byte ALIVE = 0b1;
    private static final byte DEAD = 0b0;

    private static Queue<Runnable> processQueue = new LinkedList<>();

    public static void main(String[] args) {
        Random random = new Random(135135913);
        int mapSize = 50;
        byte[][] map = performGen(getMap(mapSize, random), 2);
        int[][] biomes = new int[mapSize][mapSize];
        Map<Integer, Vertex> vertices = new HashMap<>();
        for(int x = 0; x < mapSize; x++) {
            for(int z = 0; z < mapSize; z++) {
                vertices.put(x * 500 + z, new Vertex(x, z, false));
            }
        }

        int x = random.nextInt(mapSize);
        int z = random.nextInt(mapSize);
        genBiomes(biomes, map, random, vertices, x, z);
//        int current;
//        int size;
//        int verticesVisited = 0;
//        while (true) {
//            size = 0;
//            current = getRandomNumber(random); // properly transition later
//            Queue<Vertex> queue = new LinkedList<>();
//            Vertex v1 = vertices.get(x * 500 + z);
//            queue.offer(v1);
//            if(vertices.get(x * 500 + z).isVisited())
//                break;
//            boolean run = true;
//            while(!queue.isEmpty() && run) {
//                Vertex vertex = queue.remove();
//                if(vertex.isVisited())
//                    continue;
//                vertex.setVisited(true);
//                for(int i = -1; i <= 1; i++) {
//                    for(int k = -1; k <= 1; k++) {
//                        if((i == 0 && k == 0) || (i != 0 && k != 0))
//                            continue;
//                        int dx = vertex.getX() + i;
//                        int dz = vertex.getZ() + k;
//                        Vertex v = vertices.get(dx * 500 + dz);
//                        if(!v.isVisited() && map[dx][dz] != DEAD) {
//                            // 45% chance to continue expanding if min span has been exceeded
//                            if(size < 5 || random.nextDouble() <= 0.45) {
//                                biomes[dx][dz] = current;
//                                queue.offer(v);
//                                size++;
//                            } else {
//                                System.out.println("here");
//                                run = false;
//                                x = v.getX();
//                                z = v.getZ();
//                                break;
//                            }
//                        }
//                    }
//                    if(!run)
//                        break;
//                }
//                vertices.remove(vertex.x * 500 + vertex.z);
//                verticesVisited++;
//                System.out.println(verticesVisited + "/" + (mapSize * mapSize));
//                biomes[vertex.getX()][vertex.getZ()] = current;
//                if(!run)
//                    break;
//            }
//        }

        Executors.newSingleThreadExecutor().submit(() -> {
            while(true) {
                if(processQueue.peek() != null) {
                    processQueue.remove().run();
                } else {
                    System.out.println(new GsonBuilder().create().toJson(biomes));
                }
            }
        });

    }

    private static void genBiomes(int[][] biomes, byte[][] map, Random random, Map<Integer, Vertex> vertices, int x, int z) {
        Vertex vertex = vertices.get(x * 500 + z);
        if(vertex.isVisited())
            return;
        Queue<Vertex> queue = new LinkedList<>();
        queue.offer(vertex);
        int b = getRandomNumber(random);
        int size = 0;
        while(!queue.isEmpty()) {
            Vertex v = queue.remove();
            if(size >= 10) {
                processQueue.offer(() -> genBiomes(biomes, map, random, vertices, v.x, v.z));
                continue;
            }
            for(int i = -1; i <= 1 && size < 10; i++) {
                for(int k = -1; k <= 1 && size < 10; k++) {
                    if((i == 0 && k == 0))
                        continue;
                    int dx = v.x + i;
                    int dz = v.z + k;
                    if(dx < 0 || dz < 0 || dx > map.length || dz > map.length)
                        continue;
                    Vertex v2 = vertices.get(dx * 500 + dz);
                    if(v2 != null && !v2.isVisited()) {
                        queue.offer(v2);
                        biomes[dx][dz] = b;
                        size++;
                    }
                }
            }
        }
    }

    private static byte[][] getMap(int worldSize, Random random) {
        byte[][] world = new byte[worldSize][worldSize];
        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world[i].length; j++) {
                world[i][j] = random.nextDouble() < 0.45 ? ALIVE : DEAD;
            }
        }
        return world;
    }

    private static byte[][] performGen(byte[][] map, int generations) {
        if(generations == 0)
            return map;
        byte[][] newMap = new byte[map.length][map.length];
        for(int x = 0; x < map.length; x++) {
            for(int z = 0; z < map[x].length; z++) {
                int neighbours = getNeighbourCount(map, x, z);
                if(map[x][z] == ALIVE) {
                    newMap[x][z] = neighbours < 2 ? DEAD : ALIVE;
                } else {
                    newMap[x][z] = neighbours > 3 ? ALIVE : DEAD;
                }
            }
        }
        return performGen(newMap, generations - 1);
    }

    private static int getNeighbourCount(byte[][] map, int x, int z) {
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

    private static int getRandomNumber(Random random) {
        return 1 + random.nextInt(5);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class Vertex {

        private int x, z;
        private boolean visited;
    }
}
