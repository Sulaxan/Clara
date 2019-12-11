package me.encast.clara.util.map;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import me.encast.clara.util.Util;

import java.util.List;

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

    @SerializedName("biomes")
    private String[] biomes;

    @SerializedName("island_materials")
    private SerializableMaterialData[] materials;
    @SerializedName("island_attributes")
    private int[][] islandAttributes;

    private transient List<Biome> biomeCache;

    public static int ISLAND_ATTR_NORMAL = 1;
    public static int ISLAND_ATTR_OVERHANG = 2;

    public ClaraMapResource() {
    }

    public Biome getBiome(String id) {
        if(biomeCache.size() == 0)
            throw new RuntimeException("Empty biome cache: call ClaraMapResource#loadBiomes?");
        for(Biome biome : biomeCache) {
            if(biome.getId().equals(id))
                return biome;
        }
        return null;
    }

    public void loadBiomes() {
        try {
            for(String biome : biomes) {
                Biome b = Util.loadResourceFromJar(biome, Biome.class);
                biomeCache.add(b);
            }
        } catch (Exception e) {
            // Ignore any null and invalid files
        }
    }
}
