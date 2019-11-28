package me.encast.clara.armor.impl;

import me.encast.clara.Clara;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.item.ItemRarity;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.item.ItemBuilder;
import me.encast.clara.util.item.ItemBuilderContext;
import me.encast.clara.util.resource.Locale;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class SolidifiedDaunaArmorPiece implements ClaraArmor {

    private Type type;
    private Material material;
    private String id;

    private ItemStack item;

    private static double HEALTH = 300; // +300
    private static double DEFENSE = 0.0625; // 6.25%

    public static final String HELMET_ID = "solidified_dauna_helmet";
    public static final String CHESTPLATE_ID = "solidified_dauna_chestplate";
    public static final String LEGGINGS_ID = "solidified_dauna_leggings";
    public static final String BOOTS_ID = "solidified_dauna_boots";

    public SolidifiedDaunaArmorPiece(Type type, Material material, String id) {
        this.type = type;
        this.material = material;
        this.id = id;
    }

    @Override
    public void apply(ClaraPlayer player) {
        player.addHealth(HEALTH);
        player.addDefense(DEFENSE);

    }

    @Override
    public void unapply(ClaraPlayer player) {
        player.addHealth(-HEALTH);
        player.addDefense(-DEFENSE);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName(Locale locale) {
        return "Solidified Dauna " + StringUtils.capitalize(type.name().toLowerCase());
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
                "item.armor.solidified_dauna.armor.lore",
                HEALTH,
                DEFENSE * 100
        );
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemBuilder(material, ItemBuilder.ItemType.LEATHER_ARMOUR)
                .setColor(Color.fromRGB(230, 34, 34))
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
