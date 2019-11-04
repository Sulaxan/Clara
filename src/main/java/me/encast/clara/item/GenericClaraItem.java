package me.encast.clara.item;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GenericClaraItem implements ClaraItem {

    private ItemStack item;

    private static final List<String> LORE = Lists.newArrayList("§7This is a generic item!");

    @Override
    public String getId() {
        return "generic_item";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.STANDARD;
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
        return LORE;
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
    public void loadItem(ItemStack item, NBTTagCompound extra) {
        this.item = item;
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void save(NBTTagCompound compound) {

    }

    @Override
    public boolean equals(ClaraItem item) {
        return false;
    }
}
