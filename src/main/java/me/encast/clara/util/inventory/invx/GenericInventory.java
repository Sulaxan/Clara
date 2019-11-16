package me.encast.clara.util.inventory.invx;

import com.google.common.collect.Lists;
import me.encast.clara.util.inventory.ClickData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericInventory implements UndefinedInv, ConstructableInv, InteractableInv {

    private InventoryManager manager;
    private BiConsumer<Player, InventoryManager.InvSession> open;
    private BiConsumer<Player, InventoryManager.InvSession> close;
    private Consumer<ClickContext> click;
    private Function<Player, InventoryManager.InvSession> funcSession;

    private String name = "Generic Inventory";
    private InventoryType type = InventoryType.CHEST;
    private int size = 9;

    private List<ItemContext> items = Lists.newArrayList();
    private int mode = MODE_INDIVIDUAL;

    private Inventory inventory;

    public GenericInventory(InventoryManager manager, BiConsumer<Player, InventoryManager.InvSession> open, BiConsumer<Player, InventoryManager.InvSession> close, Consumer<ClickContext> click, Function<Player, InventoryManager.InvSession> funcSession) {
        this.manager = manager;
        this.open = open;
        this.close = close;
        this.click = click;
        this.funcSession = funcSession;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public InventoryType getType() {
        return type;
    }

    @Override
    public void setInventoryType(InventoryType type) {
        this.type = type;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public ItemContext addItem(ItemStack item) {
        ItemContext context = new ItemContext(this, item, ADD_CONSTANT, null, true);
        this.items.add(context);
        return context;
    }

    @Override
    public ItemContext setItem(ItemStack item, int slot) {
        return addItem(item).setSlot(slot);
    }

    @Override
    public ItemContext setItem(ItemStack item, int slot, Consumer<ClickData> data) {
        return setItem(item, slot).setData(data);
    }

    @Override
    public ItemContext removeItem(int slot) {
        for(ItemContext ctx : items) {
            if(ctx.getSlot() == slot) {
                items.remove(ctx);
                return ctx;
            }
        }
        return null;
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void openInv(Player player) {
        manager.openInv(player, this);
    }

    @Override
    public Inventory build() {
        if((this.mode & MODE_UNIFIED) == MODE_UNIFIED && this.inventory != null) {
            return inventory;
        }
        Inventory inv;
        if(type == InventoryType.CHEST) {
            inv = Bukkit.getServer().createInventory(null, size, name);
        } else {
            inv = Bukkit.getServer().createInventory(null, type, name);
        }

        for(ItemContext ctx : items) {
            if(ctx.getSlot() == ADD_CONSTANT) {
                inv.addItem(ctx.getItem());
            } else {
                inv.setItem(ctx.getSlot(), ctx.getItem());
            }
        }
        if((this.mode & MODE_UNIFIED) == MODE_UNIFIED)
            this.inventory = inv;
        return inv;
    }

    @Override
    public void onOpen(Player player) {
        open.accept(player, funcSession.apply(player));
    }

    @Override
    public void onClose(Player player) {
        close.accept(player, funcSession.apply(player));
    }

    @Override
    public void onClick(ClickContext ctx) {
        click.accept(ctx);
    }
}
