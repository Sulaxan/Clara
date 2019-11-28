package me.encast.clara.util.item.interact;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

@Getter
public class InteractData {

    private Action action;
    private InventoryAction invAction;
    private ClickType clickType;
    private ItemStack item;
    @Setter
    private ItemStack cursorItem;
    private int slot;
    private Player player;

    @Setter
    private boolean cancel = false;

    public InteractData(Action action, InventoryAction invAction, ClickType clickType, ItemStack item, ItemStack cursorItem, int slot, Player player) {
        this.action = action;
        this.invAction = invAction;
        this.clickType = clickType;
        this.item = item;
        this.cursorItem = cursorItem;
        this.slot = slot;
        this.player = player;
    }

    public boolean isInvAction() {
        return invAction != null;
    }
}
