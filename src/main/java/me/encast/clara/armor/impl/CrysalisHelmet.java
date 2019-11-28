package me.encast.clara.armor.impl;

import me.encast.clara.Clara;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.item.ItemRarity;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.Util;
import me.encast.clara.util.item.ItemBuilder;
import me.encast.clara.util.item.ItemBuilderContext;
import me.encast.clara.util.item.interact.InteractData;
import me.encast.clara.util.item.interact.InteractableItem;
import me.encast.clara.util.resource.Locale;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CrysalisHelmet implements ClaraArmor, InteractableItem {

    private ItemStack item;

    private static double HEALTH = 100; // +100
    private static double DEFENSE = 0.02; // 2%

    public static final String ID = "crysalis_helmet";

    @Override
    public void interact(InteractData data) {
        data.getPlayer().sendMessage("§aWoah you clicked on me!");
    }

    @Override
    public void apply(ClaraPlayer player) {
        player.addHealth(HEALTH);
        player.addDefense(DEFENSE);
        player.getBukkitPlayer().sendMessage("§aAPPLIED!");

    }

    @Override
    public void unapply(ClaraPlayer player) {
        player.addHealth(-HEALTH);
        player.addDefense(-DEFENSE);
        player.getBukkitPlayer().sendMessage("§cUNAPPLIED!");
    }

    @Override
    public Type getType() {
        return Type.HELMET;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName(Locale locale) {
        return "Crysalis Helmet";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.REFINED;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public String[] getLore(Locale locale) {
        return Clara.ITEM_MSG.getMultiline(
                locale.getKey(),
                "item.armor.crysalis.helmet.lore",
                HEALTH,
                DEFENSE * 100
        );
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemBuilder(Material.LEATHER_HELMET, ItemBuilder.ItemType.LEATHER_ARMOUR)
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
    public void loadItem(ItemStack item, ItemBuilderContext context) {
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
