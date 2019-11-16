package me.encast.clara.util.inventory.invx;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class LayerInventory extends GenericInventory {

    private List<String> layers = Lists.newArrayList();
    private Map<Character, ItemStack> itemMapping = Maps.newConcurrentMap();

    public LayerInventory(InventoryManager manager, BiConsumer<Player, InventoryManager.InvSession> open, BiConsumer<Player, InventoryManager.InvSession> close, Consumer<ClickContext> click, Function<Player, InventoryManager.InvSession> funcSession) {
        super(manager, open, close, click, funcSession);
    }

    public LayerInventory withLayer(String layer) {
        if(layer.length() > 9)
            throw new UnsupportedOperationException("String length must less than or equal to 9");
        this.layers.add(layer);
        return this;
    }

    public LayerInventory map(char c, ItemStack item) {
        this.itemMapping.put(c, item);
        return this;
    }

    public void apply() {
        int slot = -1;
        for(String layer : layers) {
            slot++;
            for(char c : layer.toCharArray()) {
                if(c == ' ')
                    continue;
                ItemStack item = itemMapping.getOrDefault(c, null);
                if(item == null)
                    throw new NullPointerException("No mapping provided for character: " + c);
                setItem(item, slot);
            }
        }
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public void setInventoryType(InventoryType type) {
        //
    }
}
