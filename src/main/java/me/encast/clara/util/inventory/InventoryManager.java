package me.encast.clara.util.inventory;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Optional;

public class InventoryManager implements Listener {

    private List<AbstractInventory> registeredInventories;

    public InventoryManager() {
        registeredInventories = Lists.newArrayList();
    }

    public void registerInventory(AbstractInventory inventory) {
        registeredInventories.add(inventory);
    }

    public void unregisterInvenory(AbstractInventory inventory) {
        registeredInventories.remove(inventory);
    }

    public Optional<AbstractInventory> getInventory(Inventory inventory) {
        return registeredInventories.stream().filter(registeredInventory -> registeredInventory.getBuiltInventory() != null
                && registeredInventory.getBuiltInventory().equals(inventory)).findFirst();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        getInventory(event.getInventory()).ifPresent(abstractInventory -> abstractInventory.onOpen((Player) event.getPlayer()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Optional<AbstractInventory> optionalInventory = getInventory(event.getInventory());

        if (optionalInventory.isPresent()) {
            AbstractInventory inventory = optionalInventory.get();

            if (inventory.isCancelClicking()) {
                event.setCancelled(true);
                ClickData data = new ClickData(
                        (Player) event.getWhoClicked(),
                        event.getClickedInventory(),
                        event.getCurrentItem(),
                        event.getSlot(),
                        event.getSlotType(),
                        event.getClick(),
                        event.getAction());

                InvItem matchedItem = null;

                for (InvItem item : inventory.getItems()) {
                    if (item.getItem().hashCode() == event.getCurrentItem().hashCode() &&
                            (item.getSlot() == inventory.getADD_CONSTANT() || item.getSlot() == event.getSlot())) {
                        matchedItem = item;
                    }
                }

                if (matchedItem != null) {
                    if (matchedItem.getInvCall() != null)
                        matchedItem.getInvCall().onCall(data);

                    if (matchedItem.isCallEvent())
                        inventory.onClick(data);
                } else {
                    inventory.onClick(data);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        getInventory(event.getInventory()).ifPresent(abstractInventory -> abstractInventory.onClose((Player) event.getPlayer()));
    }

}
