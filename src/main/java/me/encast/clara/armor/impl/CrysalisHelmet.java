package me.encast.clara.armor.impl;

import com.google.common.collect.Lists;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.player.ClaraPlayer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CrysalisHelmet implements ClaraArmor {

    private double health = 100; // +100
    private double defense = 0.02; // 2%

    private final ItemStack ITEM = new ItemStack(Material.LEATHER_HELMET);

    @Override
    public void apply(ClaraPlayer player) {
        player.addHealth(health);
        player.addDefense(defense);
    }

    @Override
    public void unapply(ClaraPlayer player) {
        player.addHealth(-health);
        player.addDefense(-defense);
    }

    @Override
    public String getId() {
        return "crysalis_helmet";
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public List<String> getLore() {
        return Lists.newArrayList();
    }

    @Override
    public ItemStack getItem() {
        return ITEM;
    }

    @Override
    public void constructItem(NBTTagCompound compound) {
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void save(NBTTagCompound compound) {
        // Nothing to save since it's static
    }

    @Override
    public boolean equals(ClaraItem item) {
        return false; // Armour should be unique
    }
}
