package me.encast.clara.item.impl;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.item.MenuItem;
import me.encast.clara.util.inventory.invx.ItemContext;
import me.encast.clara.util.inventory.invx.LayerInventory;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.item.ItemBuilderContext;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

public class SkillTreeItem implements MenuItem {

    private ItemStack item;

    private static final List<String> LORE = Lists.newArrayList(
            "§7Click to view your §askill",
            "§atree§7!"
    );

    private static final LayerInventory INV;

    static {
        INV = Clara.getInstance().getInventoryManager().constructLayerInv(
                (player, inv) -> {},
                (player, inv) -> {},
                click -> click.setCancel(true)
        )
                .withLayer("aaaaiaaaa")
                .withLayer("ssasssass")
                .withLayer("asasasasa")
                .withLayer("asssasssa")
                .withLayer("ppppppppp")
                .withLayer("baaacaaan")
                .map('a', ItemContext.of(new ItemStack(Material.AIR)))
                .map('i', ItemContext.of(new ItemStack(Material.SAPLING)))
                .map('s', ItemContext.of(new ItemStack(Material.PUMPKIN_SEEDS)))
                .map('p', ItemContext.of(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5)))
                .map('b', ItemContext.of(new ItemStack(Material.CARROT_ITEM)))
                .map('c', ItemContext.of(new ItemStack(Material.BOOK_AND_QUILL), click -> {
                    click.getPlayer().sendMessage("§cClosing...");
                    click.getPlayer().closeInventory();
                }))
                .map('n', ItemContext.of(new ItemStack(Material.GOLDEN_CARROT)))
                .apply();
        INV.setName("Skill Tree");
    }

    @Override
    public UndefinedInv getInventory(Player player) {
        return INV;
    } // add player parameter to getInventory

    @Override
    public String getId() {
        return "const_skill_tree";
    }

    @Override
    public String getName(Locale locale) {
        return "Skill Tree";
    }

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
    public List<String> getLore() {
        return LORE;
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.SAPLING);
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
