package me.encast.clara.armor.impl;

import me.encast.clara.Clara;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.item.ItemRarity;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.item.ItemBuilder;
import me.encast.clara.util.item.ItemBuilderContext;
import me.encast.clara.util.item.interact.InteractData;
import me.encast.clara.util.item.interact.InteractableItem;
import me.encast.clara.util.resource.Locale;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CrysalisArmorPiece implements ClaraArmor, InteractableItem {

    private Type type;
    private Material material;
    private String id;

    private ItemStack item;

    private static double HEALTH = 100; // +100
    private static double DEFENSE = 0.02; // 2%

    public static final String HELMET_ID = "crysalis_helmet";
    public static final String CHESTPLATE_ID = "crysalis_chestplate";
    public static final String LEGGINGS_ID = "crysalis_leggings";
    public static final String BOOTS_ID = "crysalis_boots";

    public CrysalisArmorPiece(Type type, Material material, String id) {
        this.type = type;
        this.material = material;
        this.id = id;
    }

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
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName(Locale locale) {
        return "Crysalis " + StringUtils.capitalize(type.name().toLowerCase());
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
                "item.armor.crysalis.armor.lore",
                HEALTH,
                DEFENSE * 100
        );
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemBuilder(material, ItemBuilder.ItemType.LEATHER_ARMOUR)
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
