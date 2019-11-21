package me.encast.clara.util.inventory.invx;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class PageableInventory implements DefinableInv, Cloneable {

    private List<UndefinedInv> inventories;
    @Getter
    private int currentPage;

    // Whether or not the current page is about to be switched
    // Since the currentPage value is updated after an inventory is open,
    // implementations cannot tell whether the current page is staying as is
    // or updating when doing an action on the current inventory (e.g., whether
    // or not to cancel a task).
    @Getter
    private boolean switchingPages;

    public PageableInventory() {
        this.inventories = Lists.newArrayList();
    }

    public PageableInventory(List<UndefinedInv> inventories, int startPage) {
        this.inventories = inventories;
        this.currentPage = startPage;
    }

    public PageableInventory add(UndefinedInv inv) {
        this.inventories.add(inv);
        return this;
    }

    // page starts at 0
    public UndefinedInv getPage(int page) {
        if(page < this.inventories.size()) {
            return this.inventories.get(page);
        }
        return null;
    }

    public int getPageOf(UndefinedInv inv) {
        return inventories.indexOf(inv);
    }

    // page starts at 0
    public boolean hasPage(int page) {
        return page < this.inventories.size();
    }

    public void setCurrentPage(int page) {
        this.currentPage = page;
    }

    public void openAndSetNext(Player player) {
        openAndSetNext(player, currentPage + 1);
    }

    public void openAndSetNext(Player player, int page) {
        if(hasPage(page)) {
            switchingPages = true;
            getPage(page).openInv(player);
            // Do this after so that the previous menu onClose invocation gets properly invoked
            switchingPages = false;
            setCurrentPage(page);
        }
    }

    @Override
    public String getName() {
        if(!hasPage(currentPage))
            return null;
        return getPage(currentPage).getName();
    }

    @Override
    public void setName(String name) {
        if(hasPage(currentPage))
           getPage(currentPage).setName(name);
    }

    @Override
    public InventoryType getType() {
        if(!hasPage(currentPage))
            return null;
        return getPage(currentPage).getType();
    }

    @Override
    public void setInventoryType(InventoryType type) {
        if(hasPage(currentPage))
            getPage(currentPage).setInventoryType(type);
    }

    @Override
    public int getSize() {
        if(!hasPage(currentPage))
            return 0;
        return getPage(currentPage).getSize();
    }

    @Override
    public void setSize(int size) {
        if(hasPage(currentPage))
            getPage(currentPage).setSize(size);
    }

    @Override
    public List<ItemContext> getItems() {
        if(!hasPage(currentPage))
            return null;
        return getPage(currentPage).getItems();
    }

    @Override
    public void addItem(ItemContext context) {
        if(hasPage(currentPage))
            getPage(currentPage).addItem(context);
    }

    @Override
    public ItemContext addItem(ItemStack item) {
        if(!hasPage(currentPage))
            return null;
        return getPage(currentPage).addItem(item);
    }

    @Override
    public ItemContext setItem(ItemStack item, int slot) {
        if(!hasPage(currentPage))
            return null;
        return getPage(currentPage).setItem(item, slot);
    }

    @Override
    public ItemContext setItem(ItemStack item, int slot, Consumer<ClickContext> data) {
        if(!hasPage(currentPage))
            return null;
        return getPage(currentPage).setItem(item, slot, data);
    }

    @Override
    public ItemContext removeItem(int slot) {
        if(!hasPage(currentPage))
            return null;
        return getPage(currentPage).removeItem(slot);
    }

    @Override
    public int getMode() {
        if(!hasPage(currentPage))
            return 0;
        return getPage(currentPage).getMode();
    }

    @Override
    public void setMode(int mode) {
        if(hasPage(currentPage))
            getPage(currentPage).setMode(mode);
    }

    @Override
    public void openInv(Player player) {
        if(hasPage(currentPage))
            getPage(currentPage).openInv(player);
    }

    @Override
    public Inventory build() {
        if(!hasPage(currentPage) || !(getPage(currentPage) instanceof ConstructableInv))
            return null;
        return ((ConstructableInv) getPage(currentPage)).build();
    }

    @Override
    public void onOpen(Player player) {
        if(hasPage(currentPage) && getPage(currentPage) instanceof InteractableInv)
            ((InteractableInv) getPage(currentPage)).onOpen(player);
    }

    @Override
    public void onClose(Player player) {
        if(hasPage(currentPage) && getPage(currentPage) instanceof InteractableInv)
            ((InteractableInv) getPage(currentPage)).onClose(player);
    }

    @Override
    public void onClick(ClickContext ctx) {
        if(hasPage(currentPage) && getPage(currentPage) instanceof InteractableInv)
            ((InteractableInv) getPage(currentPage)).onClick(ctx);
    }

    @Override
    public PageableInventory clone() {
        try {
            return (PageableInventory) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
