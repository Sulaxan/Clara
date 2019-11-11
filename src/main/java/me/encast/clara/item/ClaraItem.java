package me.encast.clara.item;

import me.encast.clara.util.item.ItemBuilderContext;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

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

    /**
     * Id for the amount of an item.
     */
    String COUNT_KEY = "clara_item_count";

    /**
     * Id for the amount of durability an item has.
     */
    String DURABILITY_KEY = "clara_item_durability";

    /**
     * Id for the slot in which the item resides.
     */
    String SLOT_KEY = "clara_slot";

    String getId(); // this id is already set in the nbt tag compound

    String getName(Locale locale); // friendly name

    ItemRarity getRarity();

    int getAmount();

    void setAmount(int amount);

    short getDurability();

    void setDurability(short durability);

    boolean isStackable();

    List<String> getLore();

    ItemStack getNewItemInstance();

    ItemStack getItem();

    void setItem(ItemStack item);

    void loadItem(ItemStack item, ItemBuilderContext context);

    <T> T getValue(String key, Class<T> clazz);

    void save(NBTTagCompound compound);

    boolean isSimilar(ClaraItem item);

    boolean isSimilar(ItemStack item);
}
