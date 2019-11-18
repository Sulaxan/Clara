package me.encast.clara.item.impl;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.util.inventory.invx.ItemContext;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

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
                        .setDisplayName("§5Character Information")
                        .addLore(" ", "§7Hover over a button below to", "§7view more information!")
                        .build())
                )
                .map('h', ItemContext.of(new ItemBuilder(Material.APPLE, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§cHealth")
                        .setData((short) 5)
                        .addLore(
                                " ",
                                "§7You currently have",
                                "§c" + cp.getHealth() + " HP§7!",
                                " ",
                                "§7§oNote: Includes armor and any",
                                "§7§oaddtional HP buffs!"
                        )
                        .build())
                )
                .map('p', ItemContext.of(new ItemBuilder(Material.SKULL_ITEM, ItemBuilder.ItemType.SKULL)
                        .setDisplayName("§5" + player.getName() + " §a§lYOU!")
                        .setData((short) 3)
                        .setSkullOwner(player.getName())
                        .addLore(
                                " ",
                                "§7Summary of your stats:",
                                " ",
                                "§cHealth: §7" + cp.getHealth(),
                                "§9Defense: §7" + (cp.getDefense() * 100) + "%",
                                " ",
                                "§7" + cp.getEquippedArmor().size() + " piece(s) of armor equipped!",
                                "§7" + cp.getRuntimeItems().size() + " item(s) in your inventory!"
                        )
                        .build())
                )
                .map('d', ItemContext.of(new ItemBuilder(Material.IRON_CHESTPLATE, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§9Defense")
                        .setData((short) 5)
                        .addLore(
                                " ",
                                "§7You currently have",
                                "§9" + (cp.getDefense() * 100) + "% Defense§7!",
                                " ",
                                "§7Maximum obtainable defense is",
                                "§9100%§7!",
                                " ",
                                "§7§oNote: Includes armor and any",
                                "§7§oaddtional defense buffs!"
                        )
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
                .map('c', ItemContext.of(new ItemBuilder(Material.LONG_GRASS, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§cClose")
                        .setData((short) 2)
                        .addLore(" ", "§7Click to close!")
                        .build(),
                        click -> click.setCancel(true))
                )
                .apply();
    }

    @Override
    public String getId() {
        return "const_profile_item";
    }

    @Override
    public String getName(Locale locale) {
        return "Character Info";
    }

    @Override
    public List<String> getLore() {
        return LORE;
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.GHAST_TEAR);
    }
}
