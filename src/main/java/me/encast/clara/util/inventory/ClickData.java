package me.encast.clara.util.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Data class to store all inventory click information.
 */
@AllArgsConstructor
@Getter
public class ClickData {

    private Player player;
    private Inventory inventory;
    private ItemStack item;
    private int slot;
    private InventoryType.SlotType slotType;
    private ClickType clickType;
    private InventoryAction inventoryAction;
}
