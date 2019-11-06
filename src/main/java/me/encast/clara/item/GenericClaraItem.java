package me.encast.clara.item;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.encast.clara.util.item.ItemUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GenericClaraItem implements ClaraItem {

    private ItemStack item;

    @Getter
    private boolean special = false;

    private static final List<String> GENERIC_LORE = Lists.newArrayList("§7This is a generic item!");
    private static final List<String> UNGENERIC_LORE = Lists.newArrayList(
            "§7This is a §dnot-so-generic",
            "§7generic item!",
            " ",
            "§7Some say this item can be",
            "§7useful..."
    );

    private static final String SPECIAL_KEY = "clara_generic_item_special";

    @Override
    public String getId() {
        return "generic_item";
    }

    @Override
    public ItemRarity getRarity() {
        return special ? ItemRarity.GODLY : ItemRarity.STANDARD;
    }

    @Override
    public int getAmount() {
        return item.getAmount();
    }

    @Override
    public void setAmount(int amount) {
        this.item.setAmount(amount);
    }

    @Override
    public boolean isStackable() {
        return true;
    }

    @Override
    public List<String> getLore() {
        return special ? UNGENERIC_LORE : GENERIC_LORE;
    }

    @Override
    public ItemStack getNewItemInstance() {
        return null;
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
        // Change this percent to be lower later
        this.special = extra.hasKey(SPECIAL_KEY) ? extra.getBoolean(SPECIAL_KEY) : Math.random() < 0.5;
        if(this.special) {
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.item.setItemMeta(meta);
        }

        extra = ItemUtil.getRawNBT(item);
        extra.setBoolean(SPECIAL_KEY, this.special);
        Bukkit.broadcastMessage("" + extra.hasKey(ClaraItem.UUID_KEY));
        this.item = ItemUtil.applyRawNBT(item, extra);
//        net.minecraft.server.v1_8_R3.ItemStack nmsItem = ItemUtil.applyRawNBTAndGetNMS(item, extra);
//        nmsItem.addEnchantment(net.minecraft.server.v1_8_R3.Enchantment.DURABILITY, 1);
//        CraftItemStack.asCraftCopy(null).addEnchantment();
//        this.item = CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void save(NBTTagCompound compound) {

    }

    @Override
    public boolean isSimilar(ClaraItem item) {
        if(item instanceof GenericClaraItem) {
            return isSimilar(item.getItem());
        }
        return false;
    }

    @Override
    public boolean isSimilar(ItemStack item) {
        NBTTagCompound compound = ItemUtil.getRawNBT(item);
        boolean special = compound.hasKey(SPECIAL_KEY) && compound.getBoolean(SPECIAL_KEY);
        if(this.item.getType() == item.getType() && this.item.getDurability() == item.getDurability()) {
            return this.special == special;
        }
        return false;
    }
}
