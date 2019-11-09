package me.encast.clara.util.item;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemUtil {

    // Prevent initialization
    private ItemUtil() {
    }

    public static NBTTagCompound getRawNBT(ItemStack item) {
        NBTTagCompound compound = getSaveableNBT(item);
        // NBTTagCompound#getCompound(String) will either return the compound in the map
        // or a new one if it is null.
        return compound.getCompound("tag");
    }

    public static NBTTagCompound getOrSetRawNBT(ItemStack item) {
        if(item == null)
            return null;
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = i.getTag();
        if(compound == null)
            i.setTag(compound = new NBTTagCompound());

        return compound;
    }

    public static NBTTagCompound getSaveableNBT(ItemStack item) {
        NBTTagCompound compound = new NBTTagCompound();
        // save(NBTTagCompound) saves a copy of the item's NBT in the given NBTTagCompound,
        // keyed with "tag"
        CraftItemStack.asNMSCopy(item).save(compound);
        return compound;
    }

    public static ItemStack applyNBT(ItemStack item, NBTTagCompound compound) {
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(item);
        i.c(compound);
        return CraftItemStack.asBukkitCopy(i);
    }

    public static ItemStack applyRawNBT(ItemStack item, NBTTagCompound compound) {
        return CraftItemStack.asBukkitCopy(applyRawNBTAndGetNMS(item, compound));
    }

    public static ItemStack fetchAndApplyRawNBT(ItemStack item, Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = i.getTag();
        if(compound == null) {
            compound = new NBTTagCompound();
            i.setTag(compound);
        }
        consumer.accept(compound);

        return CraftItemStack.asBukkitCopy(i);
    }

    public static net.minecraft.server.v1_8_R3.ItemStack applyRawNBTAndGetNMS(ItemStack item, NBTTagCompound compound) {
        net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(item);
        i.setTag(compound);
        return i;
    }
}
