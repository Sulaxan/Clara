package me.encast.clara.util.resource.map;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

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

    @SerializedName("island_materials")
    private SerializableMaterialData[] materials;
    @SerializedName("island_attributes")
    private int[][] islandAttributes;

    public ClaraMapResource() {
    }

    @AllArgsConstructor
    @Getter
    public class SerializableMaterialData {

        private Material type;
        private byte data;

        public SerializableMaterialData() {
        }

        public MaterialData toBukkitData() {
            return new MaterialData(type, data);
        }
    }
}
