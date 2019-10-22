package me.encast.clara.item;

import com.google.common.collect.Lists;
import me.encast.clara.util.event.ArmorEquipEvent;
import me.encast.clara.util.item.ItemUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class ItemManager implements Listener {

    private List<RuntimeClaraItem> items = Lists.newArrayList();

    public ItemManager(Plugin plugin) {
        // register
    }

    public RuntimeClaraItem getRuntimeItem(UUID uuid) {
        for(RuntimeClaraItem item : items) {
            if(item.getUuid().equals(uuid))
                return item;
        }
        return null;
    }

    public ItemStack constructNewItem(ClaraItem item, boolean addAsRuntime) {
        ItemStack i = item.getNewItemInstance();
        NBTTagCompound compound = ItemUtil.getRawNBT(i);
        // Set unique id
        compound.setString(ClaraItem.UUID_KEY, UUID.randomUUID().toString());
        // Set item id
        compound.setString(ClaraItem.ITEM_ID_KEY, item.getId());

        if(addAsRuntime) {
            addToRuntime(item, compound);
        }
        return i;
    }

    public ClaraItem load(NBTTagCompound compound) {
        String itemId = compound.getString(ClaraItem.ITEM_ID_KEY);
        if(!itemId.isEmpty()) {
            // Item id should exist at this point
            ClaraItem item = getNewClaraItem(itemId);
            if(item != null) {
                ItemStack i = item.getNewItemInstance();
                if(i != null) {
                    Material type = i.getType();
                    int count = i.getAmount();
                    short damage = i.getDurability();
                    ItemUtil.applyNBT(i, compound);
                    // Restore the type, amount, and durability, just in case the compound information
                    // is outdated
                    i.setType(type);
                    i.setAmount(count);
                    i.setDurability(damage);

                    // Call loadItem in ClaraItem
                    NBTTagCompound tag = compound.getCompound("tag");
                    item.loadItem(i, compound.getCompound("tag"));

                    // No need to set item id since it should already be available

                    // Add as a runtime item
                    items.add(new RuntimeClaraItem(UUID.randomUUID(), item, tag));
                }
            }
        }
        return null;
    }

    public NBTTagCompound save(ClaraItem item) {
        NBTTagCompound compound = ItemUtil.getSaveableNBT(item.getItem());
        // For easier item loading
        compound.setString(ClaraItem.ITEM_ID_KEY, item.getId());
        return compound;
    }

    public NBTTagCompound saveAndRemoveRuntime(RuntimeClaraItem item) {
        this.items.remove(item);
        return save(item.getItem());
    }

    public ClaraItem getNewClaraItem(String id) {
        for(ClaraItemType type : ClaraItemType.VALUES) {
            if(type.getId().equals(id))
                return type.getItem();
        }

        return null;
    }

    private void addToRuntime(ClaraItem item, NBTTagCompound compound) {
        this.items.add(new RuntimeClaraItem(UUID.randomUUID(), item, compound));
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        // Merging of item ids
        if(e.getCursor() != null && e.getCurrentItem() != null) {

        }
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {

    }
}
