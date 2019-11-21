package me.encast.clara.util.inventory.invx;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.function.Consumer;

public class UpdateableInventory extends BukkitRunnable implements DefinableInv {

    private UndefinedInv inventory;
    private InventoryManager manager;
    private Player player;

    public UpdateableInventory(UndefinedInv inventory, InventoryManager manager, Player player, int delay, int period) {
        if(inventory == null)
            throw new NullPointerException("Null inventory");
        this.inventory = inventory;
        this.manager = manager;
        this.player = player;
        this.runTaskTimer(manager.getPlugin(), delay, period);
    }

    public void setInterval(int initialDelay, int period) {
        this.cancel();
        this.runTaskTimer(manager.getPlugin(), initialDelay, period);
    }

    public void update() {
        if(player == null || !player.isOnline())
            super.cancel();

        manager.openInv(player, inventory);
    }

    public void cancel() {
        super.cancel();
    }

    @Override
    public void run() {
        update();
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public void setName(String name) {
        inventory.setName(name);
    }

    @Override
    public InventoryType getType() {
        return inventory.getType();
    }

    @Override
    public void setInventoryType(InventoryType type) {
        inventory.setInventoryType(type);
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public void setSize(int size) {
        inventory.setSize(size);
    }

    @Override
    public List<ItemContext> getItems() {
        return inventory.getItems();
    }

    @Override
    public void addItem(ItemContext context) {
        inventory.addItem(context);
    }

    @Override
    public ItemContext addItem(ItemStack item) {
        return inventory.addItem(item);
    }

    @Override
    public ItemContext setItem(ItemStack item, int slot) {
        return inventory.setItem(item, slot);
    }

    @Override
    public ItemContext setItem(ItemStack item, int slot, Consumer<ClickContext> data) {
        return inventory.setItem(item, slot, data);
    }

    @Override
    public ItemContext removeItem(int slot) {
        return inventory.removeItem(slot);
    }

    @Override
    public int getMode() {
        return inventory.getMode();
    }

    @Override
    public void setMode(int mode) {
        inventory.setMode(mode);
    }

    @Override
    public void openInv(Player player) {
        inventory.openInv(player);
    }

    @Override
    public Inventory build() {
        if(!(inventory instanceof ConstructableInv))
            return null;
        return ((ConstructableInv) inventory).build();
    }

    @Override
    public void onOpen(Player player) {
        if(inventory instanceof InteractableInv)
            ((InteractableInv) inventory).onOpen(player);
    }

    @Override
    public void onClose(Player player) {
        if(inventory instanceof InteractableInv)
            ((InteractableInv) inventory).onClose(player);
    }

    @Override
    public void onClick(ClickContext ctx) {
        if(inventory instanceof InteractableInv)
            ((InteractableInv) inventory).onClick(ctx);
    }
}
