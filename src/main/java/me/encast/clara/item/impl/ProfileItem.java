package me.encast.clara.item.impl;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.inventory.invx.ItemContext;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.item.ItemBuilder;
import me.encast.clara.util.resource.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ProfileItem extends AbstractMenuItem {

    private static final List<String> LORE = Lists.newArrayList(
            " ",
            "§7Click to view §5Character",
            "§5Information§7!"
    );
    private static final ItemStack NULL_ARMOR_ITEM = new ItemBuilder(Material.STAINED_GLASS_PANE, ItemBuilder.ItemType.NORMAL)
            .setDisplayName("§7Nothing to see here!")
            .build();

    @Override
    public UndefinedInv getInventory(Player player) {
        ClaraPlayer cp = Clara.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        ClaraArmor helmet = cp.getEquippedArmor(ClaraArmor.Type.HELMET);
        ClaraArmor chestplate = cp.getEquippedArmor(ClaraArmor.Type.CHESTPLATE);
        ClaraArmor leggings = cp.getEquippedArmor(ClaraArmor.Type.LEGGINGS);
        ClaraArmor boots = cp.getEquippedArmor(ClaraArmor.Type.BOOTS);

        ItemStack helmetItem = (helmet == null ? NULL_ARMOR_ITEM : helmet.getItem());
        ItemStack chestplateItem = (chestplate == null ? NULL_ARMOR_ITEM : chestplate.getItem());
        ItemStack leggingsItem = (leggings == null ? NULL_ARMOR_ITEM : leggings.getItem());
        ItemStack bootsItem = (boots == null ? NULL_ARMOR_ITEM : boots.getItem());

        return Clara.getInstance().getInventoryManager().constructLayerInv(
                (p, session) -> {},
                (o, session) -> {},
                click -> click.setCancel(true)
        )
                .withLayer("    i    ")
                .withLayer("         ")
                .withLayer("  h p d  ")
                .withLayer("  sssss  ")
                .withLayer("^>       ")
                .withLayer("<v  c   m")
                .map('i', ItemContext.of(new ItemBuilder(Material.GHAST_TEAR, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName(Clara.ITEM_MSG.get(cp.getLocale(), "item.menu.profile.name"))
                        .addLore(Clara.ITEM_MSG.getMultiline(cp.getLocale(), "item.menu.profile.menu_icon.lore"))
                        .build())
                )
                .map('h', ItemContext.of(new ItemBuilder(Material.APPLE, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName(Clara.ITEM_MSG.get(cp.getLocale(), "item.menu.profile.icon.health.name"))
                        .addLore(Clara.ITEM_MSG.getMultiline(
                                cp.getLocale(),
                                "item.menu.profile.icon.health.lore",
                                cp.getHealth()
                        ))
                        .build())
                )
                .map('p', ItemContext.of(new ItemBuilder(Material.SKULL_ITEM, ItemBuilder.ItemType.SKULL)
                        .setDisplayName(Clara.ITEM_MSG.get(cp.getLocale(), "item.menu.profile.icon.summary.name", player.getName()))
                        .setData((short) 3)
                        .setSkullOwner(player.getName())
                        .addLore(Clara.ITEM_MSG.getMultiline(
                                cp.getLocale(),
                                "item.menu.profile.icon.summary.lore",
                                cp.getHealth(),
                                cp.getDefense() * 100,
                                cp.getUnlockedSpells().size(),
                                cp.getEquippedArmor().size(),
                                cp.getRuntimeItems().size()
                        ))
                        .build())
                )
                .map('d', ItemContext.of(new ItemBuilder(Material.IRON_CHESTPLATE, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName(Clara.ITEM_MSG.get(cp.getLocale(), "item.menu.profile.icon.defense.name"))
                        .addLore(Clara.ITEM_MSG.getMultiline(
                                cp.getLocale(),
                                "item.menu.profile.icon.defense.lore",
                                cp.getDefense() * 100
                        ))
                        .build())
                )
                .map('s', ItemContext.of(new ItemBuilder(Material.INK_SACK, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§7Spell")
                        .setData((short) 8)
                        .addLore(" ", "§7Coming Soon!")
                        .build())
                )
                .map('^', ItemContext.of(helmetItem))
                .map('>', ItemContext.of(chestplateItem))
                .map('<', ItemContext.of(leggingsItem))
                .map('v', ItemContext.of(bootsItem))
                // make an item constant for closing
                .map('c', ItemContext.of(new ItemBuilder(Material.LONG_GRASS, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName(Clara.ITEM_MSG.get(cp.getLocale(), "item.menu.general.close.name"))
                        .setData((short) 2)
                        .addLore(Clara.ITEM_MSG.getMultiline(cp.getLocale(), "item.menu.general.close.lore"))
                        .build(),
                        click -> click.getPlayer().closeInventory())
                )
                .map('m', ItemContext.of(new ItemBuilder(Material.BOOK, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§4Slain Mob History")
                        .addLore(" ", "§7Coming Soon!")
                        .build())
                )
                .apply();
    }

    @Override
    public String getId() {
        return "const_profile_item";
    }

    @Override
    public String getName(Locale locale) {
        return Clara.ITEM_MSG.get(locale, "item.menu.profile.name");
    }

    @Override
    public String[] getLore(Locale locale) {
        return Clara.ITEM_MSG.getMultiline(locale, "item.menu.profile.lore");
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.GHAST_TEAR);
    }
}
