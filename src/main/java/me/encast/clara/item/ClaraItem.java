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

    /**
     * Non-unique item id. This identifies the internal corresponding
     * item.
     */
    String ITEM_ID_KEY = "clara_item_id";

    String getId(); // this id is already set in the nbt tag compound

    ItemRarity getRarity();

    int getAmount();

    void setAmount(int amount);

    boolean isStackable();

    List<String> getLore();

    ItemStack getNewItemInstance();

    ItemStack getItem();

    void loadItem(ItemStack item, NBTTagCompound extra);

    <T> T getValue(String key, Class<T> clazz);

    void save(NBTTagCompound compound);

    boolean equals(ClaraItem item);
}
