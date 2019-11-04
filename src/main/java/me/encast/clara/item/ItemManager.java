package me.encast.clara.item;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.event.ArmorEquipEvent;
import me.encast.clara.util.inventory.ItemStackUtil;
import me.encast.clara.util.item.ItemUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class ItemManager implements Listener {

    /*
        Possibly fix the issue where you can't pick up an item if your inventory is full
        (if you already have that item in your inventory)

        Possibly fix the issue where you can't stack the same item if you already have a stack of it
        (as in, the new stack gets a new uuid and therefore can't be mixed)
        Possible fix:
            Set same item UUIDs to the same thing OR check if a player tries to combine an item,
            and if so, change the item UUIDs and stack it
     */

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
        setDefaultNBT(compound, item, uuid);
        i = ItemUtil.applyRawNBT(i, compound);
        applyItemData(i, item);
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
                    applyItemData(i, item);

                    UUID uuid = UUID.randomUUID();

                    // Call loadItem in ClaraItem
                    NBTTagCompound tag = compound.getCompound("tag");
                    setDefaultNBT(tag, item, uuid);
                    item.loadItem(i, tag);

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

    public RuntimeClaraItem addToRuntimeFromExisting(ClaraPlayer player, ItemStack item) {
        NBTTagCompound compound = ItemUtil.getOrSetRawNBT(item);
        UUID uuid;
        ClaraItem claraItem;
        if(!compound.getString(ClaraItem.ITEM_ID_KEY).isEmpty() &&
                !compound.getString(ClaraItem.UUID_KEY).isEmpty()) {
            claraItem = getNewClaraItem(compound.getString(ClaraItem.ITEM_ID_KEY));
            if(claraItem == null)
                claraItem = new GenericClaraItem();
            uuid = UUID.fromString(compound.getString(ClaraItem.UUID_KEY));
        } else {
            uuid = UUID.randomUUID();
            claraItem = new GenericClaraItem();
        }


        setDefaultNBT(compound, claraItem, uuid);
        item = ItemUtil.applyRawNBT(item, compound);
        claraItem.loadItem(item, compound);
        applyItemData(claraItem.getItem(), claraItem);

        if(claraItem.getAmount() == 0)
            claraItem.setAmount(1);

        // Combine items after the item is loaded into ClaraItem, to ensure item changes
        // don't get ignored
        Bukkit.broadcastMessage("ITEM IS " + ((GenericClaraItem) claraItem).isSpecial());
        combineItems(player, claraItem);
        if(claraItem.getAmount() == 0) {
            player.getBukkitPlayer().updateInventory();
            return null;
        }
        RuntimeClaraItem runtimeItem = new RuntimeClaraItem(uuid, claraItem, ItemUtil.getRawNBT(claraItem.getItem()));
        player.addRuntimeItem(runtimeItem);
        return runtimeItem;
    }

//    private void combineItems(ClaraPlayer player, ItemStack item) {
//        for(RuntimeClaraItem i : player.getRuntimeItems()) {
//            if(i.getItem().isStackable() && i.getItem().getAmount() < 64) {
//                ItemStack runtime = i.getItem().getItem();
//                if(i.getItem().isSimilar(runtime)) {
//                    int remaining = 64 - i.getItem().getAmount();
//                    i.getItem().setAmount(Math.min(64, i.getItem().getAmount() + item.getAmount()));
//                    if(remaining < item.getAmount()) {
//                        item.setAmount(item.getAmount() - remaining);
//                        updateItem(player.getBukkitPlayer(), i);
//                    } else {
//                        item.setAmount(0);
//                        updateItem(player.getBukkitPlayer(), i);
//                        return;
//                    }
//                }
//            }
//        }
//    }

    private void combineItems(ClaraPlayer player, ClaraItem item) {
        for(RuntimeClaraItem i : player.getRuntimeItems()) {
            if(i.getItem().isStackable() && i.getItem().getAmount() < 64 && i.getItem().isSimilar(item)) {
                int remaining = 64 - i.getItem().getAmount();
                i.getItem().setAmount(Math.min(64, i.getItem().getAmount() + item.getAmount()));
                if(remaining < item.getAmount()) {
                    item.setAmount(item.getAmount() - remaining);
                    updateItem(player.getBukkitPlayer(), i);
                } else {
                    item.setAmount(0);
                    updateItem(player.getBukkitPlayer(), i);
                    return;
                }
            }
        }
    }

    private void updateItem(Player player, RuntimeClaraItem item) {
        ItemStack[] contents = player.getInventory().getContents();
        for(int i = 0; i < contents.length; i++) {
            if(contents[i] != null) {
                String uuid = ItemStackUtil.getMetadataValue(contents[i], ClaraItem.UUID_KEY);
                if(uuid != null && item.getNbt().getString(ClaraItem.UUID_KEY).equalsIgnoreCase(uuid)) {
                    contents[i] = item.getItem().getItem();
                }
            }
        }

        player.getInventory().setContents(contents);
        player.updateInventory();
    }

    private void setDefaultNBT(NBTTagCompound compound, ClaraItem item, UUID uuid) {
        compound.setString(ClaraItem.UUID_KEY, uuid.toString());
        // Just in case it is a generic item
        if(item != null)
            compound.setString(ClaraItem.ITEM_ID_KEY, item.getId());
    }

    private void applyItemData(ItemStack item, ClaraItem ci) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = Lists.newArrayList();
        ItemRarity rarity = ItemRarity.STANDARD;
        if(ci != null) {
            // Just in case it is a generic item
            rarity = ci.getRarity();
            lore = Lists.newArrayList(ci.getLore());
            lore.add(" ");
        } else {
            lore.add("§7This is a generic item!");
        }
        lore.add("§7⚔ " + rarity.getDisplay());
        meta.setLore(lore);
        item.setItemMeta(meta);
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
            if(item != null && item.getItem() instanceof ClaraArmor) {
                ((ClaraArmor) item.getItem()).unapply(cp);
            }
        }

        if(e.getNewArmorPiece() != null) {
            item = getRuntimeItem(cp, e.getNewArmorPiece());
            if(item != null && item.getItem() instanceof ClaraArmor) {
                ((ClaraArmor) item.getItem()).apply(cp);
                // Simulating equipping armour
                p.playSound(p.getLocation(), Sound.NOTE_STICKS, 5, 1);
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        ClaraPlayer player = Clara.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
        e.setCancelled(true);
        e.getItem().remove();
        // Simulating item pickup
        RuntimeClaraItem item = addToRuntimeFromExisting(player, e.getItem().getItemStack());
        if(item != null)
            item.giveItem(p);
        p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 5, 1);
    }

    @EventHandler
    public void onItemRemove(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        ClaraPlayer player = Clara.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
        RuntimeClaraItem item = getRuntimeItem(player, e.getItemDrop().getItemStack());
        if(item != null) {
            int amount = e.getItemDrop().getItemStack().getAmount();
            if(amount == item.getItem().getAmount()) {
                player.removeRuntimeItem(item);
            } else {
                item.getItem().setAmount(item.getItem().getAmount() - amount);
                updateItem(p, item);
            }
        }
    }
}
