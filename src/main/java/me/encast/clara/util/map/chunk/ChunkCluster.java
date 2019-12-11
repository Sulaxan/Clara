package me.encast.clara.util.map.chunk;

import lombok.Getter;

@Getter
public class ChunkCluster {

    private int clusterX;
    private int clusterZ;
    private ClaraChunk[] chunks = new ClaraChunk[SIZE * SIZE];

    public static final int SIZE = 32;

    public ChunkCluster(int clusterX, int clusterZ) {
        this.clusterX = clusterX;
        this.clusterZ = clusterZ;
    }

    public String getId() {
        return clusterX + "_" + clusterZ;
    }

    public ClaraChunk getClaraChunk(int x, int z) {
        return chunks[Math.abs(x) * SIZE + Math.abs(z)];
    }

    public void updateChunk(ClaraChunk chunk) {
        chunks[(Math.abs(chunk.getX()) % SIZE) * SIZE + (Math.abs(chunk.getZ()) % SIZE)] = chunk;
    }

    public static String getClusterId(int chunkX, int chunkZ) {
        return (chunkX / SIZE) + "_" + (chunkZ / SIZE);
    }

    public static String getClusterIdFromClusterCoord(int x, int z) {
        return x + "_" + z;
    }
}
