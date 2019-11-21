package me.encast.clara.item;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.event.ArmorEquipEvent;
import me.encast.clara.util.item.ItemBuilderContext;
import me.encast.clara.util.item.ItemUtil;
import me.encast.clara.util.item.interact.InteractData;
import me.encast.clara.util.item.interact.InteractableItem;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
        if(item == null)
            return null;
        String uuid = ItemUtil.getOrDefaultRawNBT(item).getString(ClaraItem.UUID_KEY);
        if(uuid != null && !uuid.isEmpty())
            try {
                return getRuntimeItem(player, UUID.fromString(uuid));
            } catch (Exception e) { }

        return null;
    }

    public ItemStack constructNewItem(ClaraPlayer player, ClaraItem item, boolean addAsRuntime) {
        ItemStack i = item.getNewItemInstance();
        NBTTagCompound compound = ItemUtil.getOrDefaultRawNBT(i);
        // Set unique id
        UUID uuid = UUID.randomUUID();

        ItemBuilderContext context = new ItemBuilderContext(compound, i.getItemMeta());
        item.loadItem(i, context);
        compound = context.getCompound();
        i = item.getItem();
        applyItemData(i, context.getMeta(), item);
        setDefaultNBT(compound, item, uuid);

        AtomicReference<NBTTagCompound> ref = new AtomicReference<>(compound);
        i = ItemUtil.fetchAndApplyRawNBT(i, tag -> {
            copyUniqueKeyNBT(tag, ref.get());
            return ref.get();
        });

        item.setItem(i);

        if(addAsRuntime) {
            player.addRuntimeItem(new RuntimeClaraItem(uuid, item, compound));
        }
        return i;
    }

    public ClaraItem load(ClaraPlayer player, NBTTagCompound compound, boolean giveItem) {
        String itemId = compound.getString(ClaraItem.ITEM_ID_KEY);
        if(!itemId.isEmpty()) {
            // Item id should exist at this point
            ClaraItem item = getNewClaraItem(itemId);

            if(item == null) {
                item = new GenericClaraItem();
            }

            ItemStack i = item.getNewItemInstance();
            if(i != null) {
                int count = Math.max(compound.getInt(ClaraItem.COUNT_KEY), 1);
                int slot = compound.hasKey(ClaraItem.SLOT_KEY) ? compound.getInt(ClaraItem.SLOT_KEY) : -1;

                removeUnneededSaveNBT(compound);
                copyUniqueKeyNBT(ItemUtil.getRawNBT(i), compound);


                UUID uuid = UUID.randomUUID();

//                setDefaultNBT(compound, null, uuid); // make it null so the item id isn't set twice
//                i = ItemUtil.applyRawNBT(i, compound);

                // Call loadItem in ClaraItem

                ItemBuilderContext context = new ItemBuilderContext(compound, i.getItemMeta());
                item.loadItem(i, context);

                i = item.getItem(); // just in case it changes
                setDefaultNBT(compound, null, uuid);
                i = ItemUtil.applyRawNBT(i, compound);
                applyItemData(i, context.getMeta(), item);

                compound = ItemUtil.getRawNBT(i);


                item.setItem(i);

                // No need to set item id since it should already be available

                // Add as a runtime item
                RuntimeClaraItem runtime = getSimilarRuntime(player, item);
                if(runtime != null) {
                    runtime.getItem().setAmount(runtime.getItem().getAmount() + count); // update count
                } else {
                    player.addRuntimeItem(new RuntimeClaraItem(uuid, item, compound));
                }

                if(giveItem && slot >= 0) {
                    ItemStack adjusted = item.getItem().clone();
                    adjusted.setAmount(count);
                    player.getBukkitPlayer().getInventory().setItem(slot, adjusted);
                }
            }

            return item;
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

    public void removeUnneededSaveNBT(NBTTagCompound compound) {
        compound.remove(ClaraItem.COUNT_KEY);
        compound.remove(ClaraItem.DURABILITY_KEY);
        compound.remove(ClaraItem.SLOT_KEY);
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
                // Cloning the item so that data isn't overwritten
                addToRuntimeFromExisting(player, item.clone(), false, giveItem);
            }
            return;
        }

        NBTTagCompound compound = ItemUtil.getOrDefaultRawNBT(item);
        UUID uuid = UUID.randomUUID();
        ClaraItem claraItem = null;
        // We can ignore the item UUID since it'll either be matched or a new one will be generated
        if(!compound.getString(ClaraItem.ITEM_ID_KEY).isEmpty()) {
            claraItem = getNewClaraItem(compound.getString(ClaraItem.ITEM_ID_KEY));
        }

        if(claraItem == null)
            claraItem = new GenericClaraItem();

        ItemBuilderContext context = new ItemBuilderContext(compound, item.getItemMeta());
        claraItem.loadItem(item, context);
        compound = context.getCompound();

        claraItem.setAmount(Math.max(1, claraItem.getAmount()));

        RuntimeClaraItem runtimeItem = getSimilarRuntime(player, claraItem);

        if(runtimeItem != null) {
            runtimeItem.getItem().setAmount(runtimeItem.getItem().getAmount() + claraItem.getAmount());
        } else {
            runtimeItem = new RuntimeClaraItem(uuid, claraItem, null);
            applyItemData(claraItem.getItem(), context.getMeta(), claraItem);

            setDefaultNBT(compound, claraItem, uuid); // apply default nbt after just in case it got replaced

            AtomicReference<NBTTagCompound> tempRef = new AtomicReference<>(compound);
            claraItem.setItem(ItemUtil.fetchAndApplyRawNBT(claraItem.getItem(), tag -> {
                copyUniqueKeyNBT(tag, tempRef.get());
                return tempRef.get();
            }));

            runtimeItem.setNbt(compound);
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

    public RuntimeClaraItem getSimilarRuntime(ClaraPlayer player, ClaraItem item) {
        if(!item.isStackable())
            return null;
        for(RuntimeClaraItem runtime : player.getRuntimeItems()) {
            if(item.isSimilar(runtime.getItem())) // Switching around since nbt data isn't set (perhaps set nbt data beforehand?)
            //if(runtime.getItem().isSimilar(item))
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

    private void setDefaultNBT(NBTTagCompound compound, ClaraItem item, UUID uuid) {
        compound.setString(ClaraItem.UUID_KEY, uuid.toString());
        // Just in case it is a generic item
        if(item != null)
            compound.setString(ClaraItem.ITEM_ID_KEY, item.getId());
    }

    // Copies all unique keys in from into to
    // If a key is a list, it'll add all the key elements into to (assuming that same key is a list)
    // If a key is a compound, it'll recursively add the unique keys
    private void copyUniqueKeyNBT(NBTTagCompound from, NBTTagCompound to) {
        for(String key : from.c()) {
            if(!to.hasKey(key)) {
                to.set(key, from.get(key));
            } else {
                NBTBase fromBase = from.get(key);
                NBTBase toBase = to.get(key);
                if(fromBase instanceof NBTTagList && toBase instanceof NBTTagList) {
                    NBTTagList fromList = (NBTTagList) fromBase;
                    NBTTagList toList = (NBTTagList) toBase;
                    // Check list types (f)
                    if(fromList.f() == toList.f()) {
                        try {
                            Field list = fromList.getClass().getDeclaredField("list");
                            list.setAccessible(true);
                            for(NBTBase base : (List<NBTBase>) list.get(fromList)) {
                                toList.add(base);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if(fromBase instanceof NBTTagCompound && toBase instanceof NBTTagCompound) {
                    copyUniqueKeyNBT((NBTTagCompound) fromBase, (NBTTagCompound) toBase);
                }
            }
            // Possibly account for adding elements to a list or inner compound
        }
    }

    private void applyItemData(ItemStack item, ItemMeta meta, ClaraItem ci) {
        List<String> lore = Lists.newArrayList();
        ItemRarity rarity = ItemRarity.STANDARD;
        if(ci != null) {
            // Just in case it is a generic item
            rarity = ci.getRarity();

            if(ci.getName(Locale.ENGLISH) != null)
                meta.setDisplayName((rarity != ItemRarity.NONE ? rarity.getColor() : ChatColor.GREEN) +
                        ci.getName(Locale.ENGLISH));

            lore = Lists.newArrayList(ci.getLore());
            if(rarity != ItemRarity.NONE)
                lore.add(" ");
        } else {
            lore.add("§7This is a generic item!");
        }
        if(rarity != ItemRarity.NONE)
            lore.add("§7⚔ " + rarity.getDisplay());


        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInvItemClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.PLAYER)
            return;
        Player p = (Player) e.getWhoClicked();
        ClaraPlayer cp = Clara.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
        ItemStack item = e.getCurrentItem();
        if(item != null && item.getType() != Material.AIR) {
            RuntimeClaraItem runtime = getRuntimeItem(cp, item);
            if(runtime != null && runtime.getItem() instanceof InteractableItem) {
                // add data for the inventory
                InteractData data = new InteractData(null, e.getAction(), item, e.getCursor(), p);
                ((InteractableItem) runtime.getItem()).interact(data);
                e.setCancelled(data.isCancel());
            }
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
            int amount = e.getItemDrop().getItemStack().getAmount();
            item.getItem().setAmount(item.getItem().getAmount() - amount);
            if(item.getItem().getAmount() <= 0) {
                player.removeRuntimeItem(item);
            }
        }
    }
}
