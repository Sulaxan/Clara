package me.encast.clara.item.impl;

import me.encast.clara.Clara;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.resource.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        return Clara.ITEM_MSG.get(locale, "item.menu.drop.name");
    }

    @Override
    public String[] getLore(Locale locale) {
        return Clara.ITEM_MSG.getMultiline(locale, "item.menu.drop.lore");
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.CAULDRON_ITEM);
    }
}
