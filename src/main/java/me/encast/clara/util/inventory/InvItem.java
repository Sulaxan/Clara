package me.encast.clara.util.inventory;

import me.encast.clara.util.concurrent.Callable;
import org.bukkit.inventory.ItemStack;

public class InvItem {

    private ItemStack item;
    private int slot;
    private Callable<ClickData> invCall;
    private boolean callEvent;

    /**
     * Constructs a new instance of InvItem.
     *
     * @param item The item to be allocated in the inventory.
     * @param slot The slot to set the item in the inventory.
     * @param invCall The call to be invoked when the item is clicked.
     * @param callEvent Whether {@link AbstractInventory#onClick(ClickData)} should
     * be invoked when the item is clicked.
     */
    public InvItem(ItemStack item, int slot, Callable<ClickData> invCall, boolean callEvent) {
        this.item = item;
        this.slot = slot;
        this.invCall = invCall;
        this.callEvent = callEvent;
    }

    /**
     * Gets the item to be allocated in the inventory.
     *
     * @return The instance of {@link ItemStack} that will be
     * used to allocate a slot in the inventory.
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the slot that the item will be placed in.
     *
     * @return The slot of the item.
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Gets the callable to be invoked when the item is interacted
     * with.
     *
     * @return The callable to be invoked.
     */
    public Callable<ClickData> getInvCall() {
        return invCall;
    }

    /**
     * Gets whether {@link AbstractInventory#onClick(ClickData)} should
     * be invoked when the item is clicked.
     *
     * @return Whether {@link AbstractInventory#onClick(ClickData)} will
     * be invoked.
     */
    public boolean isCallEvent() {
        return callEvent;
    }
}
