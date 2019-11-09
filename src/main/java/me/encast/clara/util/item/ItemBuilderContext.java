package me.encast.clara.util.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.meta.ItemMeta;

@AllArgsConstructor
@Getter
@Setter
public class ItemBuilderContext {

    private NBTTagCompound compound;
    private ItemMeta meta;
}
