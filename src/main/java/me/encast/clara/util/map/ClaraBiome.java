package me.encast.clara.util.map;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.List;

@AllArgsConstructor
@Getter
public class ClaraBiome {

    private int id;
    private String name;
    private double temperature;
    private double rarity;
    @SerializedName("min_span")
    private int minSpan;
    private Biome minecraftBiome;
    private List<SerializableMaterialData> materials;
    @SerializedName("block_populator")
    private String blockPopulatorName;

    private transient BlockPopulator blockPopulator;

    public static ClaraBiome DEFAULT_BIOME;

    public static int RESERVED_ID = 0;

    public ClaraBiome() {
    }

    public void loadBlockPopulator() {
        blockPopulator = BlockPopulator.match(blockPopulatorName);
    }

    static {
        DEFAULT_BIOME = new ClaraBiome(
                0,
                "clara_default",
                10,
                0.5,
                50,
                Biome.PLAINS,
                Lists.newArrayList(new SerializableMaterialData(Material.GRASS, (byte) 0)),
                "",
                null);
    }
}
