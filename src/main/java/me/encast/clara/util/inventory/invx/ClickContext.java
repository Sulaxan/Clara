package me.encast.clara.util.inventory.invx;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

@Getter
public class ClickContext {

    private Player player;
    private InventoryManager.InvSession session;
    private ItemStack item;
    private int slot;
    private InventoryType.SlotType slotType;
    private ClickType clickType;
    private InventoryAction invAction;
    private boolean cancel = false;

    public ClickContext(Player player, InventoryManager.InvSession session, ItemStack item, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction invAction) {
        this.player = player;
        this.session = session;
        this.item = item;
        this.slot = slot;
        this.slotType = slotType;
        this.clickType = clickType;
        this.invAction = invAction;
    }
}
