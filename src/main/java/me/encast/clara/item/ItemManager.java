package me.encast.clara.item;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class ItemManager implements Listener {

    public ItemManager(Plugin plugin) {
        // register
    }

    public NBTTagCompound getNBT(ItemStack item) {
        return null;
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        // Merging of item ids
        if(e.getCursor() != null && e.getCurrentItem() != null) {

        }
    }
}
