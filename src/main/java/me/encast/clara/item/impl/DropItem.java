package me.encast.clara.item.impl;

import me.encast.clara.Clara;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.item.RuntimeClaraItem;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.inventory.InvSize;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.item.interact.InteractData;
import me.encast.clara.util.item.interact.InteractableItem;
import me.encast.clara.util.resource.Locale;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DropItem extends AbstractMenuItem implements InteractableItem {

    /*
     * Possibly make it so that the player who drops the item has x seconds
     * to pickup their item before someone else can pick it up.
     */

    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private static final int PICKUP_DELAY_SECONDS = 2;

    @Override
    public void interact(InteractData data) {
        ClaraPlayer cp = Clara.getInstance().getPlayerManager().getPlayer(data.getPlayer().getUniqueId());
        if(data.getCursorItem() != null && data.getCursorItem().getType() != Material.AIR) {
            data.getPlayer().getWorld().dropItem(data.getPlayer().getLocation(), data.getCursorItem())
                    .setPickupDelay(20 * PICKUP_DELAY_SECONDS);
            drop(data.getPlayer(), data.getCursorItem());
            // Set the current item to air so that the player doesn't pick it up
            data.getPlayer().getInventory().setItem(data.getSlot(), AIR);
            // Disabling the player's ability to pickup items so the item about to get deleted
            // doesn't get stacked with any items the player picks up
            data.getPlayer().setCanPickupItems(false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    data.getPlayer().getInventory().setItem(data.getSlot(), getItem());
                    data.getPlayer().setCanPickupItems(true);
                }
            }.runTaskLater(Clara.getInstance(), 5);
        } else if(data.isInvAction() && (data.getClickType() == ClickType.SHIFT_LEFT ||
                data.getClickType() == ClickType.SHIFT_RIGHT)) {
            data.setCancel(true);
            UndefinedInv inv = Clara.getInstance().getInventoryManager().constructInv(
                    (player, session) -> {},
                    (player, session) -> {
                        for(ItemStack item : session.getBukkitInv()) {
                            if(item != null && item.getType() != Material.AIR) {
                                data.getPlayer().getWorld().dropItem(data.getPlayer().getLocation(), item)
                                        .setPickupDelay(20 * 2);
                                // remove runtime item from the player instance
                                drop(player, item);
                            }
                        }
                    },
                    ctx -> {}
            );

            inv.setName(Clara.ITEM_MSG.get(cp.getLocale(), "item.menu.drop.bulk.name"));
            inv.setSize(InvSize.THREE_ROWS.getSlots());
            Clara.getInstance().getInventoryManager().openInv(data.getPlayer(), inv);
        } else {
            data.setCancel(true);
            data.getPlayer().sendMessage(Clara.GENERAL_MSG.get(cp.getLocale(), "player.item.drop.no_item"));
            data.getPlayer().playSound(data.getPlayer().getLocation(), Sound.NOTE_PLING, 5, 0.1f);
        }
    }

    private void drop(Player player, ItemStack item) {
        ClaraPlayer cp = Clara.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        RuntimeClaraItem runtime = Clara.getInstance().getItemManager().dropItem(cp, item);
        if(runtime != null) {
            player.sendMessage(Clara.GENERAL_MSG.get(
                    cp.getLocale(),
                    "player.item.drop",
                    item.getAmount(),
                    runtime.getItem().getName(cp.getLocale())
            ));
        } else {
            player.sendMessage(Clara.GENERAL_MSG.get(cp.getLocale(), "player.item.drop.invalid"));
        }
    }

    @Override
    public UndefinedInv getInventory(Player player) {
        return null;
    }

    @Override
    public String getId() {
        return "const_drop_item";
    }

    @Override
    public String getName(Locale locale) {
        return Clara.ITEM_MSG.get(locale, "item.menu.drop.name");
    }

    @Override
    public String[] getLore(Locale locale) {
        return Clara.ITEM_MSG.getMultiline(locale, "item.menu.drop.lore", PICKUP_DELAY_SECONDS);
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.CAULDRON_ITEM);
    }
}
