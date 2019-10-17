package me.encast.clara.util.inventory;

import com.google.common.collect.Lists;
import lombok.Data;
import me.encast.clara.util.concurrent.Callable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public abstract class AbstractInventory implements Listener {

    private String name;
    private InvSize invSize;
    private List<InvItem> items;

    private Inventory builtInventory;

    private boolean cancelClicking = true;

    private final int ADD_CONSTANT = -1;

    public AbstractInventory(String name, InvSize invSize) {
        this.name = name;
        this.invSize = invSize;
        this.items = Lists.newCopyOnWriteArrayList();
    }

    public AbstractInventory addItem(InvItem item) {
        this.items.add(item);
        return this;
    }

    public AbstractInventory addItem(ItemStack item) {
        this.items.add(new InvItem(item, ADD_CONSTANT, null, true));
        return this;
    }

    public AbstractInventory addItem(ItemStack item, int slot) {
        this.items.add(new InvItem(item, slot, null, true));
        return this;
    }

    public AbstractInventory addItem(ItemStack item, Callable<ClickData> call) {
        this.items.add(new InvItem(item, ADD_CONSTANT, call, true));
        return this;
    }

    public AbstractInventory addItem(ItemStack item, int slot, Callable<ClickData> call) {
        this.items.add(new InvItem(item, slot, call, true));
        return this;
    }

    public AbstractInventory addItem(ItemStack item, Callable<ClickData> call, boolean callEvent) {
        this.items.add(new InvItem(item, ADD_CONSTANT, call, callEvent));
        return this;
    }

    public AbstractInventory addItem(ItemStack item, int slot, Callable<ClickData> call, boolean callEvent) {
        this.items.add(new InvItem(item, slot, call, callEvent));
        return this;
    }

    public AbstractInventory removeItem(int slot) {
        for(InvItem item : items) {
            if(item.getSlot() == slot)
                items.remove(item);
        }
        return this;
    }

    public AbstractInventory buildInventory() {
        Inventory inv = Bukkit.getServer().createInventory(null, this.invSize.getSlots(), this.name);

        for(InvItem item : this.items) {
            if(item.getSlot() != ADD_CONSTANT) {
                inv.setItem(item.getSlot(), item.getItem());
            } else {
                inv.addItem(item.getItem());
            }
        }

        this.builtInventory = inv;

        return this;
    }

    public AbstractInventory rebuildInventory() {
        if(this.builtInventory != null) {
            this.builtInventory.clear();
            for(InvItem item : this.items) {
                if(item.getSlot() != ADD_CONSTANT) {
                    this.builtInventory.setItem(item.getSlot(), item.getItem());
                } else {
                    this.builtInventory.addItem(item.getItem());
                }
            }
        }

        return this;
    }

    public AbstractInventory register() {
//        .getInstance().getGame().getInventoryManager().registerInventory(this);
        return this;
    }

    public Inventory getBuiltInventory() {
        if(builtInventory == null) {
            buildInventory();
        }

        return builtInventory;
    }

    public void openInventory(Player player) {
        onOpen(player);
        player.openInventory(getBuiltInventory());
    }

    public abstract void onOpen(Player player);

    public abstract void onClick(ClickData data);

    public abstract void onClose(Player player);
}
