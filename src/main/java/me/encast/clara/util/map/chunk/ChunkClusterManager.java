package me.encast.clara.util.map.chunk;

import me.encast.clara.Clara;
import org.bukkit.World;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.WeakHashMap;

public class ChunkClusterManager {

    private WeakHashMap<String, ChunkCluster> clusterCache = new WeakHashMap<>();

    private static final String CLUSTER_DIRECTORY = "clara_chunk_clusters";

    public ChunkClusterManager() {
    }

    public ChunkCluster getCluster(World world, int x, int z) {
        ChunkCluster cluster = clusterCache.getOrDefault(world.getName() + "_" + ChunkCluster.getClusterId(x, z), null);
        if(cluster != null)
            return cluster;

        File clusterDir = new File(world.getWorldFolder().getAbsolutePath() + File.separatorChar + CLUSTER_DIRECTORY);
        if(!clusterDir.exists())
            clusterDir.mkdir();

        File clusterFile = new File(clusterDir.getAbsolutePath() + File.separatorChar + ChunkCluster.getClusterId(x, z) + ".json");
        if(clusterFile.exists()) {
            try (FileReader reader = new FileReader(clusterFile)) {
                cluster = Clara.GSON.fromJson(reader, ChunkCluster.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cluster = new ChunkCluster(Math.abs(x) % ChunkCluster.SIZE, Math.abs(z) % ChunkCluster.SIZE);
            try (FileWriter writer = new FileWriter(clusterFile)) {
                Clara.PRETTY_GSON.toJson(cluster, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Save to cache
        clusterCache.put(world.getName() + "_" + ChunkCluster.getClusterId(x, z), cluster);

        return cluster;
    }

    public void updateCluster(World world, ClaraChunk chunk) {
        ChunkCluster cluster = getCluster(world, chunk.getX(), chunk.getZ());
        cluster.updateChunk(chunk);
        for(ClaraChunk c : cluster.getChunks()) {
           if(c == null)
               return;
        }

        pushChangesToDisk(world, cluster);
    }

    public void pushChangesToDisk(World world, ChunkCluster cluster) {
        File clusterDir = new File(world.getWorldFolder().getAbsolutePath() + File.separatorChar + CLUSTER_DIRECTORY);
        if(!clusterDir.exists())
            clusterDir.mkdir();

        File clusterFile = new File(clusterDir.getAbsolutePath() + File.separatorChar + cluster.getId() + ".json");
        clusterFile.delete();

        try (FileWriter writer = new FileWriter(clusterFile)) {
            Clara.PRETTY_GSON.toJson(cluster, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
