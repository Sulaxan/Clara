package me.encast.clara.util.inventory.invx;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ClickContext {

    private Player player;
    private InventoryManager.InvSession session;
    private ItemStack item;
    private int slot;
    private InventoryType.SlotType slotType;
    private ClickType clickType;
    private InventoryAction invAction;

    @Setter
    private boolean cancel = false;

    @Setter
    private List<Runnable> processesAtEnd = Lists.newArrayList();

    public ClickContext(Player player, InventoryManager.InvSession session, ItemStack item, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction invAction) {
        this.player = player;
        this.session = session;
        this.item = item;
        this.slot = slot;
        this.slotType = slotType;
        this.clickType = clickType;
        this.invAction = invAction;
    }

    // Ran at the very end of the event (after the event is cancelled or not)
    public void addEndProcess(Runnable runnable) {
        processesAtEnd.add(runnable);
    }
}
