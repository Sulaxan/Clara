package me.encast.clara.util.inventory.invx;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class UpdateableInventory extends BukkitRunnable implements DefinableInv {

    private UndefinedInv inventory;
    private InventoryManager manager;
    private Player player;

    private Function<UndefinedInv, UndefinedInv> updateFunction;

    private boolean running = true;
    @Getter
    private boolean updating = false;

    public UpdateableInventory(UndefinedInv inventory, InventoryManager manager, Player player) {
        this(inventory, manager, player, 0, 20);
    }

    public UpdateableInventory(UndefinedInv inventory, InventoryManager manager, Player player, int delay, int period) {
        this(inventory, manager, player, delay, period, true);
    }

    public UpdateableInventory(UndefinedInv inventory, InventoryManager manager, Player player, int delay, int period, boolean running) {
        if(inventory == null)
            throw new NullPointerException("Null inventory");
        this.inventory = inventory;
        this.manager = manager;
        this.player = player;
        this.running = running;
        this.runTaskTimer(manager.getPlugin(), delay, period);
    }

    public void setInterval(int delay, int period) {
        super.runTaskTimer(manager.getPlugin(), delay, period);
    }

    public void setUpdateFunction(Function<UndefinedInv, UndefinedInv> updateFunction) {
        this.updateFunction = updateFunction;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void update() {
        if(player == null || !player.isOnline())
            super.cancel();

        updating = true;
        if(updateFunction != null)
            this.inventory = updateFunction.apply(inventory);
        manager.openInv(player, inventory);
        updating = false;
    }

    public void cancel() {
        super.cancel();
    }

    @Override
    public void run() {
        if(running)
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
