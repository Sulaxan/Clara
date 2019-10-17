package me.encast.clara.util.inventory;

import me.encast.clara.util.item.MetaEntry;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtil {

    // Prevent initialization
    private ItemStackUtil() {
    }

    public static ItemStack setMetadata(ItemStack item, String key, String value) {
        return setMetadata(item, new MetaEntry(key, value));
    }

    public static ItemStack setMetadata(ItemStack item, MetaEntry... entries) {
        if(entries != null && entries.length >= 1) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            NBTTagCompound compound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
            for(MetaEntry entry : entries) {
                compound.set(entry.getKey(), new NBTTagString(entry.getValue()));
            }
            nmsItem.setTag(compound);
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
        return item;
    }

    public static String getMetadataValue(ItemStack item, String key) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = nmsItem.getTag();
        if(compound != null)
            return compound.getString(key);
        return null;
    }

    public static boolean hasMetadata(ItemStack item, String key) {
        return getMetadataValue(item, key) != null;
    }
}
