package me.encast.clara.item;

import me.encast.clara.util.item.ItemBuilderContext;
import me.encast.clara.util.resource.Locale;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMenuItem implements MenuItem {

    private ItemStack item;

    @Override
    public int getAmount() {
        return 0;
    }

    @Override
    public void setAmount(int amount) {

    }

    @Override
    public short getDurability() {
        return 0;
    }

    @Override
    public void setDurability(short durability) {

    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public List<String> getLore(Locale locale) {
        return Collections.emptyList();
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
        return false;
    }

    @Override
    public boolean isSimilar(ItemStack item) {
        return false;
    }
}
