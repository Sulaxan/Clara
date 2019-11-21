package me.encast.clara.util.inventory.invx;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public interface NullInv extends DefinableInv {

    @Override
    default Inventory build() {
        return null;
    }

    @Override
    default void onOpen(Player player) {

    }

    @Override
    default void onClose(Player player) {

    }

    @Override
    default void onClick(ClickContext ctx) {

    }

    @Override
    default String getName() {
        return null;
    }

    @Override
    default void setName(String name) {

    }

    @Override
    default InventoryType getType() {
        return null;
    }

    @Override
    default void setInventoryType(InventoryType type) {

    }

    @Override
    default int getSize() {
        return 0;
    }

    @Override
    default void setSize(int size) {

    }

    @Override
    default List<ItemContext> getItems() {
        return null;
    }

    @Override
    default void addItem(ItemContext context) {

    }

    @Override
    default ItemContext addItem(ItemStack item) {
        return null;
    }

    @Override
    default ItemContext setItem(ItemStack item, int slot) {
        return null;
    }

    @Override
    default ItemContext setItem(ItemStack item, int slot, Consumer<ClickContext> data) {
        return null;
    }

    @Override
    default ItemContext removeItem(int slot) {
        return null;
    }

    @Override
    default int getMode() {
        return 0;
    }

    @Override
    default void setMode(int mode) {

    }

    @Override
    default void openInv(Player player) {

    }
}
