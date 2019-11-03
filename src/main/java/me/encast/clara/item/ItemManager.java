package me.encast.clara.item;

import com.google.gson.GsonBuilder;
import me.encast.clara.Clara;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.event.ArmorEquipEvent;
import me.encast.clara.util.inventory.ItemStackUtil;
import me.encast.clara.util.item.ItemUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ItemManager implements Listener {

    public ItemManager(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public RuntimeClaraItem getRuntimeItem(ClaraPlayer player, UUID uuid) {
        for(RuntimeClaraItem item : player.getRuntimeItems()) {
            if(item.getUuid().equals(uuid))
                return item;
        }
        return null;
    }

    public RuntimeClaraItem getRuntimeItem(ClaraPlayer player, ItemStack item) {
        String uuid = ItemStackUtil.getMetadataValue(item, ClaraItem.UUID_KEY);
        if(uuid != null)
            return getRuntimeItem(player, UUID.fromString(uuid));

        return null;
    }

    public ItemStack constructNewItem(ClaraPlayer player, ClaraItem item, boolean addAsRuntime) {
        ItemStack i = item.getNewItemInstance();
        NBTTagCompound compound = ItemUtil.getOrSetRawNBT(i);
        // Set unique id
        UUID uuid = UUID.randomUUID();
        compound.setString(ClaraItem.UUID_KEY, uuid.toString());
        // Set item id
        compound.setString(ClaraItem.ITEM_ID_KEY, item.getId());
        i = ItemUtil.applyRawNBT(i, compound);
        if(addAsRuntime) {
            player.addRuntimeItem(new RuntimeClaraItem(uuid, item, compound));
        }
        return i;
    }

    public ClaraItem load(ClaraPlayer player, NBTTagCompound compound) {
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

                    UUID uuid = UUID.randomUUID();

                    // Call loadItem in ClaraItem
                    NBTTagCompound tag = compound.getCompound("tag");
                    tag.setString(ClaraItem.UUID_KEY, uuid.toString());
                    item.loadItem(i, compound.getCompound("tag"));

                    // No need to set item id since it should already be available

                    // Add as a runtime item
                    player.addRuntimeItem(new RuntimeClaraItem(uuid, item, tag));
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

    public NBTTagCompound saveAndRemoveRuntime(ClaraPlayer player, RuntimeClaraItem item) {
        player.removeRuntimeItem(item);
        return save(item.getItem());
    }

    public ClaraItem getNewClaraItem(String id) {
        for(ClaraItemType type : ClaraItemType.VALUES) {
            if(type.getId().equals(id))
                return type.getItem();
        }

        return null;
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        // Merging of item ids
        if(e.getCursor() != null && e.getCurrentItem() != null) {

        }
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e) {
        Player p = e.getPlayer();
        ClaraPlayer cp = Clara.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
        if(cp == null)
            return;

        RuntimeClaraItem item;
        if(e.getOldArmorPiece() != null) {
            item = getRuntimeItem(cp, e.getOldArmorPiece());
            if(item.getItem() instanceof ClaraArmor) {
                ((ClaraArmor) item.getItem()).unapply(cp);
            }
        }

        if(e.getNewArmorPiece() != null) {
            item = getRuntimeItem(cp, e.getNewArmorPiece());
            if(item.getItem() instanceof ClaraArmor) {
                ((ClaraArmor) item.getItem()).apply(cp);
            }
        }
    }
}