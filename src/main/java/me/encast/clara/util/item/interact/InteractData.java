package me.encast.clara.util.item.interact;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

@Getter
public class InteractData {

    private Action action;
    private InventoryAction invAction;
    private ItemStack item;
    private ItemStack cursorItem;
    private Player player;

    public InteractData(Action action, InventoryAction invAction, ItemStack item, ItemStack cursorItem, Player player) {
        this.action = action;
        this.invAction = invAction;
        this.item = item;
        this.cursorItem = cursorItem;
        this.player = player;
    }

    public boolean isInvAction() {
        return invAction != null;
    }
}
