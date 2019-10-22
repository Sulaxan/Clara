package me.encast.clara.item;

import com.google.common.collect.Lists;
import me.encast.clara.util.item.ItemUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class ItemManager implements Listener {

    private List<RuntimeClaraItem> items = Lists.newArrayList();

    public ItemManager(Plugin plugin) {
        // register
    }

    public ItemStack constructItem(ClaraItem item) {
        ItemStack i = item.getNewItemInstance();
        NBTTagCompound compound = ItemUtil.getRawNBT(i);
        // Set unique id
        compound.setString(ClaraItem.UUID_KEY, UUID.randomUUID().toString());
        // Set item id
        compound.setString(ClaraItem.ITEM_ID_KEY, item.getId());
        return i;
    }

    public ClaraItem load(NBTTagCompound compound) {

    }

    public NBTTagCompound save(ItemStack item) {
        return ItemUtil.getSaveableNBT(item);
    }



    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        // Merging of item ids
        if(e.getCursor() != null && e.getCurrentItem() != null) {

        }
    }
}
