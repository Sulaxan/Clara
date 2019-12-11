package me.encast.clara.util.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Biome {

    private String id;
    private String name;
    private double temperature;
    private double rarity;
    private List<SerializableMaterialData> materials;
    private BlockPopulator blockPopulator;

    public Biome() {
    }
}
