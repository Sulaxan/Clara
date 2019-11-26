package me.encast.clara.item.impl;

import com.google.common.collect.Lists;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.resource.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DropItem extends AbstractMenuItem {

    @Override
    public UndefinedInv getInventory(Player player) {
        return null;
    }

    @Override
    public String getId() {
        return "const_drop_item";
    }

    @Override
    public String getName(Locale locale) {
        return "§cDrop Item";
    }

    @Override
    public List<String> getLore(Locale locale) {
        return Clara;
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.CAULDRON_ITEM);
    }
}
