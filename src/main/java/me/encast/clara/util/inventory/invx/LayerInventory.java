package me.encast.clara.util.inventory.invx;

import me.encast.clara.util.inventory.ClickData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class LayerInventory implements DefinableInv {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public InventoryType getType() {
        return null;
    }

    @Override
    public void setInventoryType(InventoryType type) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void setSize(int size) {

    }

    @Override
    public ItemContext addItem(ItemStack item) {
        return null;
    }

    @Override
    public ItemContext setItem(ItemStack item, int slot) {
        return null;
    }

    @Override
    public ItemContext setItem(ItemStack item, int slot, Consumer<ClickData> data) {
        return null;
    }

    @Override
    public ItemContext removeItem(int slot) {
        return null;
    }

    @Override
    public int getMode() {
        return 0;
    }

    @Override
    public void setMode(int mode) {

    }

    @Override
    public void openInv(Player player) {

    }

    @Override
    public Inventory build() {
        return null;
    }

    @Override
    public void onOpen(Player player) {

    }

    @Override
    public void onClose(Player player) {

    }

    @Override
    public void onClick(ClickContext ctx) {

    }
}
