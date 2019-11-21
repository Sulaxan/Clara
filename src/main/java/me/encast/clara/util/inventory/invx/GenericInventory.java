package me.encast.clara.util.inventory.invx;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericInventory implements DefinableInv {

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
    public List<ItemContext> getItems() {
        return items;
    }

    @Override
    public void addItem(ItemContext context) {
        this.items.add(context);
        context.setInventory(this);
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
    public ItemContext setItem(ItemStack item, int slot, Consumer<ClickContext> data) {
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

        Set<Integer> alloted = Sets.newHashSet();
        List<ItemContext> toProcess = Lists.newArrayList();
        for(ItemContext ctx : items) {
            if(ctx.getSlot() == ADD_CONSTANT) {
                toProcess.add(ctx);
            } else if(ctx.getSlot() < getSize()) {
                inv.setItem(ctx.getSlot(), ctx.getItem());
                alloted.add(ctx.getSlot());
            }
        }

        // Possibly add whether the slot should be recomputed in ItemContext
        // (just in case another item is set later on and it collides with an add item)
        if(toProcess.size() > 1) {
            int slot = 0;
            for(ItemContext ctx : toProcess) {
                do {
                    slot++;
                } while(alloted.contains(slot));
                if(slot >= getSize())
                    break;
                ctx.setSlot(slot);
                inv.setItem(slot, ctx.getItem());
                slot++; // so that the same slot doesn't get overwritten
            }
        }

        if((this.mode & MODE_UNIFIED) == MODE_UNIFIED)
            this.inventory = inv;
        return inv;
    }

    @Override
    public void onOpen(Player player) {
        if(open != null)
            open.accept(player, funcSession.apply(player));
    }

    @Override
    public void onClose(Player player) {
        if(close != null)
            close.accept(player, funcSession.apply(player));
    }

    @Override
    public void onClick(ClickContext ctx) {
        if(ctx != null)
            click.accept(ctx);
    }
}
