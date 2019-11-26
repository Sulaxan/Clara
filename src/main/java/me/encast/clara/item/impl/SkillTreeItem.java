package me.encast.clara.item.impl;

import me.encast.clara.Clara;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.util.inventory.invx.ItemContext;
import me.encast.clara.util.inventory.invx.LayerInventory;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.resource.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkillTreeItem extends AbstractMenuItem {

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
        return Clara.ITEM_MSG.get(locale, "item.menu.skill_tree.name");
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
    public String[] getLore(Locale locale) {
        return Clara.ITEM_MSG.getMultiline(locale, "item.menu.skill_tree.lore");
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.SAPLING);
    }
}
