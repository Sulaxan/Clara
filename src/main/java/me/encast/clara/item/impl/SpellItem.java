package me.encast.clara.item.impl;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.menu.SpellsMenu;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

public class SpellItem extends AbstractMenuItem {

    private static final List<String> LORE = Lists.newArrayList(
            "§7Click to view your §6Spells§7"
    );

    @Override
    public UndefinedInv getInventory(Player player) {
        return new SpellsMenu(Clara.getInstance().getPlayerManager().getPlayer(player.getUniqueId()));
    }

    @Override
    public String getId() {
        return "const_spells_menu_item";
    }

    @Override
    public String getName(Locale locale) {
        return "Spells";
    }

    @Override
    public List<String> getLore() {
        return LORE;
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.EYE_OF_ENDER);
    }
}
