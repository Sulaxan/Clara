package me.encast.clara.item;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.encast.clara.util.item.ItemBuilderContext;
import me.encast.clara.util.item.ItemUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GenericClaraItem implements ClaraItem {

    private ItemStack item;

    @Getter
    private boolean special = false;
    private boolean corrupted = false;

    private static final List<String> GENERIC_LORE = Lists.newArrayList("§7This is a generic item!");
    private static final List<String> UNGENERIC_LORE = Lists.newArrayList(
            "§7This is a §dnot-so-generic",
            "§7generic item!",
            " ",
            "§7Some say this item can be",
            "§7useful..."
    );
    private static final List<String> CORRUPTED_LORE = Lists.newArrayList(
            "§7???"
    );

    // Keys can only have max length of 26 characters
    private static final String SPECIAL_KEY = "clara_gen_item_special";
    private static final String CORRUPTED_KEY = "clara_gen_item_corrupt";
    private static final String TYPE_KEY = "clara_gen_item_type";
    private static final String ENCHANT_KEY = "clara_gen_item_ench";

    @Override
    public String getId() {
        return "generic_item";
    }

    @Override
    public ItemRarity getRarity() {
        return special ? ItemRarity.GODLY : this.corrupted ? ItemRarity.CORRUPTED : ItemRarity.STANDARD;
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
    public short getDurability() {
        return item.getDurability();
    }

    @Override
    public void setDurability(short durability) {
        // maybe make a way to change durability of all similar items in the player's inventory
        item.setDurability(durability);
    }

    @Override
    public boolean isStackable() {
        return item.getType().getMaxStackSize() != 1;
    }

    @Override
    public List<String> getLore() {
        return special ? UNGENERIC_LORE : this.corrupted ? CORRUPTED_LORE : GENERIC_LORE;
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.STONE);
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
        if(this.item == null)
            this.item = getNewItemInstance();

        NBTTagCompound compound = context.getCompound();

        String type = context.getCompound().getString(TYPE_KEY);

        if(!type.isEmpty()) {
            compound.remove(TYPE_KEY);
            this.item.setType(Material.matchMaterial(type));
        }

        // Change this percent to be lower later
        this.special = compound.hasKey(SPECIAL_KEY) ? context.getCompound().getBoolean(SPECIAL_KEY) : Math.random() < 0.5;
        if(!this.special)
            this.corrupted = compound.hasKey(CORRUPTED_KEY) ? compound.getBoolean(CORRUPTED_KEY) : Math.random() < 0.1;
        if(this.special || this.corrupted) {
            ItemMeta meta = context.getMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            // meta will be automatically set
        }

//        context.setCompound(compound = ItemUtil.getRawNBT(item));
        compound.setBoolean(SPECIAL_KEY, this.special);
        compound.setBoolean(CORRUPTED_KEY, this.corrupted);
        this.item = ItemUtil.applyRawNBT(item, compound);
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void save(NBTTagCompound compound) {
        NBTTagCompound raw = ItemUtil.getRawNBT(item);
        compound.setString(TYPE_KEY, item.getType().name());
        compound.setBoolean(SPECIAL_KEY, this.special);
        compound.setBoolean(CORRUPTED_KEY, this.corrupted);
        compound.set(ENCHANT_KEY, raw.get("ench"));
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
        boolean corrupted = compound.hasKey(CORRUPTED_KEY) && compound.getBoolean(CORRUPTED_KEY);
        if(this.item.getType() == item.getType() && this.item.getDurability() == item.getDurability()) {
            return this.special == special && this.corrupted == corrupted;
        }
        return false;
    }
}
