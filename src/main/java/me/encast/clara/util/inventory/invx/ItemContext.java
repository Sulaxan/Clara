package me.encast.clara.util.inventory.invx;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@AllArgsConstructor
@Getter
public class ItemContext {

    private UndefinedInv inventory;
    private ItemStack item;
    private int slot;
    private Consumer<ClickContext> data;
    private boolean invokeEvent;

    public ItemContext setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public ItemContext setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public ItemContext setData(Consumer<ClickContext> data) {
        this.data = data;
        return this;
    }

    public ItemContext setInvokeEvent(boolean invokeEvent) {
        this.invokeEvent = invokeEvent;
        return this;
    }

    public UndefinedInv complete() {
        return this.inventory;
    }
}
