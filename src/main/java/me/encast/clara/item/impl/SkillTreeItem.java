package me.encast.clara.item.impl;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.util.inventory.invx.ItemContext;
import me.encast.clara.util.inventory.invx.LayerInventory;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

public class SkillTreeItem extends AbstractMenuItem {

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
                .withLayer("    i    ")
                .withLayer("ss sss ss")
                .withLayer(" s s s s ")
                .withLayer(" sss sss ")
                .withLayer("ppppppppp")
                .withLayer("b   c   n")
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
    }

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
}
