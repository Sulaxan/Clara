package me.encast.clara.util.item;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class StaticItemContext {

    private static Map<Integer, NBTTagCompound> staticNbt = Maps.newConcurrentMap();

    // Prevent initialization
    private StaticItemContext() {
    }

    public static NBTTagCompound getCompound(ItemStack item) {
        NBTTagCompound compound = staticNbt.get(item.hashCode());
        if(compound == null) {
            compound = ItemUtil.getRawNBT(item);
            staticNbt.put(item.hashCode(), compound);
        }

        return compound;
    }
}
