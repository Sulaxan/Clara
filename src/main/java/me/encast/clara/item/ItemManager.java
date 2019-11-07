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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
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
            try {
                return getRuntimeItem(player, UUID.fromString(uuid));
            } catch (Exception e) {
                return null;
            }

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

    public NBTTagCompound saveItem(ItemStack item, RuntimeClaraItem runtime) {
        NBTTagCompound saveNBT = new NBTTagCompound();
        saveNBT.setString(ClaraItem.ITEM_ID_KEY, runtime.getItem().getId());
        saveNBT.setInt(ClaraItem.COUNT_KEY, item.getAmount());
        saveNBT.setShort(ClaraItem.DURABILITY_KEY, runtime.getItem().getDurability());
        runtime.getItem().save(saveNBT);
        return saveNBT;
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

    // addIndividually just makes it so that if the item count is > 1, the method will be called again
    // with the same item
    // This is done so that if, for example, 5 items were picked up, generic item might make all 5
    // special items, when we actually just want at least 1 being special
    public void addToRuntimeFromExisting(ClaraPlayer player, ItemStack item, boolean addIndividually, boolean giveItem) {
        if(addIndividually && item.getAmount() > 1) {
            int amount = item.getAmount();
            item.setAmount(1);
            for(int i = 0; i < amount; i++) {
                addToRuntimeFromExisting(player, item, false, giveItem);
            }
            return;
        }
        NBTTagCompound compound = ItemUtil.getOrSetRawNBT(item);
        UUID uuid = UUID.randomUUID();
        ClaraItem claraItem;
        if(!compound.getString(ClaraItem.ITEM_ID_KEY).isEmpty() &&
                !compound.getString(ClaraItem.UUID_KEY).isEmpty()) {
            claraItem = getNewClaraItem(compound.getString(ClaraItem.ITEM_ID_KEY));
            if(claraItem == null) {
                claraItem = new GenericClaraItem();
            } else {
                uuid = UUID.fromString(compound.getString(ClaraItem.UUID_KEY));
            }
        } else {
            claraItem = new GenericClaraItem();
        }

        setDefaultNBT(compound, claraItem, uuid);
        claraItem.loadItem(item, compound);
        applyItemData(claraItem.getItem(), claraItem);

        if(claraItem.getAmount() == 0)
            claraItem.setAmount(1);

        RuntimeClaraItem runtimeItem = getSimilarRuntime(player, claraItem);
        if(runtimeItem != null) {
            runtimeItem.getItem().setAmount(claraItem.getAmount());
        } else {
            runtimeItem = new RuntimeClaraItem(uuid, claraItem, ItemUtil.getRawNBT(claraItem.getItem()));
            player.addRuntimeItem(runtimeItem);
        }

        if(giveItem) {
            Map<Integer, ItemStack> remaining = normalizeAndAdd(player, runtimeItem.getItem());
            // Accounting for items that could not be added
            if(remaining.size() > 1) {
                // Should only be 1, so we can safely decrement the runtime item
                for(Map.Entry<Integer, ItemStack> entry : remaining.entrySet()) {
                    runtimeItem.getItem().setAmount(runtimeItem.getItem().getAmount() - entry.getValue().getAmount());
                }
            }
        }
    }

    public UUID getItemUUID(ClaraPlayer player, ClaraItem item) {
        if(!item.isStackable())
            return null;

        for(RuntimeClaraItem runtime : player.getRuntimeItems()) {
            if(runtime.getItem().isSimilar(item))
                return runtime.getUuid();
        }

        return null;
    }

    public UUID getItemUUID(ClaraPlayer player, ItemStack item) {
        for(RuntimeClaraItem runtime : player.getRuntimeItems()) {
            if(runtime.getItem().isSimilar(item))
                return runtime.getUuid();
        }

        return null;
    }

    public RuntimeClaraItem getSimilarRuntime(ClaraPlayer player, ClaraItem item) {
        if(!item.isStackable())
            return null;
        for(RuntimeClaraItem runtime : player.getRuntimeItems()) {
            if(runtime.getItem().isSimilar(item))
                return runtime;
        }
        return null;
    }

    // sets item count to one and gives the item to the player
    private Map<Integer, ItemStack> normalizeAndAdd(ClaraPlayer player, ClaraItem item) {
        ItemStack i = item.getItem().clone();
        i.setAmount(1);
        return player.getBukkitPlayer().getInventory().addItem(i);
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
    public void onArmorEquip(ArmorEquipEvent e) {
        Player p = e.getPlayer();
        ClaraPlayer cp = Clara.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
        if(cp == null)
            return;

        RuntimeClaraItem item;
        if(e.getOldArmorPiece() != null) {
            item = getRuntimeItem(cp, e.getOldArmorPiece());
            if(item != null && item.getItem() instanceof ClaraArmor) {
                cp.removeEquippedArmor(item);
            }
        }

        if(e.getNewArmorPiece() != null) {
            item = getRuntimeItem(cp, e.getNewArmorPiece());
            if(item != null && item.getItem() instanceof ClaraArmor) {
                if(cp.addEquippedArmor(item)) {
                    // Simulating armour equip sound
                    p.playSound(p.getLocation(), Sound.NOTE_STICKS, 5, 1);
                }
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
        addToRuntimeFromExisting(player, e.getItem().getItemStack(), true, true);
        p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 5, 1);
    }

    @EventHandler
    public void onItemRemove(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        ClaraPlayer player = Clara.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
        RuntimeClaraItem item = getRuntimeItem(player, e.getItemDrop().getItemStack());
        if(item != null) {
            Bukkit.broadcastMessage("NOT NULL!");
            int amount = e.getItemDrop().getItemStack().getAmount();
            item.getItem().setAmount(item.getItem().getAmount() - amount);
            if(item.getItem().getAmount() <= 0) {
                player.removeRuntimeItem(item);
            }
        }
    }
}
