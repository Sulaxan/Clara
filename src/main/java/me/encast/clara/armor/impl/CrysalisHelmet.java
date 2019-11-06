package me.encast.clara.armor.impl;

import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.item.ItemRarity;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.Util;
import me.encast.clara.util.item.ItemBuilder;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class CrysalisHelmet implements ClaraArmor {

    private ItemStack item;

    private double health = 100; // +100
    private double defense = 0.02; // 2%

    public static final String ID = "crysalis_helmet";

    @Override
    public void apply(ClaraPlayer player) {
        player.addHealth(health);
        player.addDefense(defense);
        player.getBukkitPlayer().sendMessage("§aAPPLIED!");

    }

    @Override
    public void unapply(ClaraPlayer player) {
        player.addHealth(-health);
        player.addDefense(-defense);
        player.getBukkitPlayer().sendMessage("§cUNAPPLIED!");
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.REFINED;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    // make improvements later
    @Override
    public List<String> getLore() {
        return Util.formatStringToList("This helmet provides you with §a" + health + " §ahealth §7and §5+" +
                (defense * 100) + "% §5defense§7.", 30)
                .stream()
                .map(str -> "§7" + str)
                .collect(Collectors.toList());
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemBuilder(Material.LEATHER_HELMET, ItemBuilder.ItemType.LEATHER_ARMOUR)
                .setDisplayName("§5Crysalis Helmet")
                .setColor(Color.WHITE)
                .setUnbreakable(true)
                .addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public void loadItem(ItemStack item, NBTTagCompound extra) {
        this.item = item;
        // Nothing to change about the item
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
    public boolean isSimilar(ClaraItem item) {
        return false; // Armour should be unique
    }

    @Override
    public boolean isSimilar(ItemStack item) {
        return false;
    }
}
