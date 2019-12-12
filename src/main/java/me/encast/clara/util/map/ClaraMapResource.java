package me.encast.clara.util.map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import me.encast.clara.Clara;
import me.encast.clara.util.Util;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Getter
@Setter
public class ClaraMapResource {

    @SerializedName("chance_to_live")
    private double chanceToLive = 0.45;
    @SerializedName("starvation_limit")
    private int starvationLimit = 2;
    @SerializedName("birth_count")
    private int birthCount = 3;
    @SerializedName("generations")
    private int generations = 2;
    @SerializedName("world_size")
    private int worldSize = 1000;

    @SerializedName("island_start_y")
    private int islandStartY = 90;
    @SerializedName("island_end_y")
    private int islandEndY = 99;

    @SerializedName("dungeon_spawn_rate")
    private double dungeonSpawnRate = 0.05;

    @SerializedName("biomes")
    private String[] biomes = new String[0];

    @SerializedName("island_materials")
    private SerializableMaterialData[] materials;
    @SerializedName("island_attributes")
    private int[][] islandAttributes;

    private transient Map<Integer, ClaraBiome> biomeCache = Maps.newHashMap();

    public static int ISLAND_ATTR_NORMAL = 1;
    public static int ISLAND_ATTR_OVERHANG = 2;

    public ClaraMapResource() {
    }

    public ClaraBiome getBiome(int id) {
        if(biomeCache.size() == 0)
            throw new RuntimeException("Empty biome cache: call ClaraMapResource#loadBiomes?");
        return biomeCache.getOrDefault(id, null);
    }

    public List<ClaraBiome> getBiomes(Predicate<ClaraBiome> predicate) {
        List<ClaraBiome> biomes = Lists.newArrayList();
        for(ClaraBiome biome : biomeCache.values()) {
            if(predicate.test(biome))
                biomes.add(biome);
        }
        return biomes;
    }

    public void loadBiomes() {
        try {
            biomeCache.put(0, ClaraBiome.DEFAULT_BIOME);
            for(String biome : biomes) {
                ClaraBiome b = Util.loadResourceFromJar(biome, ClaraBiome.class);
                if(b.getId() != ClaraBiome.RESERVED_ID)
                    biomeCache.put(b.getId(), b);
            }
        } catch (Exception e) {
            // Ignore any null and invalid files
            e.printStackTrace();
        }
    }
}
