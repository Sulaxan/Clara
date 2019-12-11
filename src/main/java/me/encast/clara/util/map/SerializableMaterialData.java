package me.encast.clara.util.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

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
