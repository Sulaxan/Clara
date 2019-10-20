package me.encast.clara.item;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ClaraItem {

    /**
     * Custom unique id each item is assigned. Similar items will
     * get their UUIDs matched.
     */
    String UUID_KEY = "clara_item_uuid";

    String getId(); // this id is already set in the nbt tag compound

    int getAmount();

    void setAmount(int amount);

    boolean isStackable();

    List<String> getLore();

    ItemStack getItem();

    void constructItem(NBTTagCompound compound);

    <T> T getValue(String key, Class<T> clazz);

    void save(NBTTagCompound compound);

    boolean equals(ClaraItem item);
}
