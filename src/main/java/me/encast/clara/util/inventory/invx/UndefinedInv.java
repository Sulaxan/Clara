package me.encast.clara.util.inventory.invx;

import me.encast.clara.util.inventory.ClickData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface UndefinedInv {

    int ADD_CONSTANT = 0xFFA01; // Prefix: 0xFF  Type: A (const)  Action: 01

    // Inventory Modes
    int MODE_UNIFIED = 0b01; // Action: 01

    int MODE_INDIVIDUAL = 0b10; // Action: 10

    String getName();

    void setName(String name);

    InventoryType getType();

    void setInventoryType(InventoryType type);

    int getSize();

    void setSize(int size);

    ItemContext addItem(ItemStack item);

    ItemContext setItem(ItemStack item, int slot);

    ItemContext setItem(ItemStack item, int slot, Consumer<ClickData> data);

    ItemContext removeItem(int slot);

    int getMode();

    void setMode(int mode);

    void openInv(Player player);
}
