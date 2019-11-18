package me.encast.clara.item.impl;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.item.AbstractMenuItem;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.util.inventory.invx.ItemContext;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.item.ItemBuilder;
import me.encast.clara.util.item.ItemBuilderContext;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

public class InfoItem extends AbstractMenuItem {

    private static final List<String> LORE = Lists.newArrayList(
            " ",
            "§7Click to view §aInfo Menu§7!"
    );

    @Override
    public UndefinedInv getInventory(Player player) {
        return Clara.getInstance().getInventoryManager().constructLayerInv(
                (p, session) -> {},
                (o, session) -> {},
                click -> {}
        )
                .withLayer("    i    ")
                .withLayer("         ")
                .withLayer("    s    ")
                .withLayer("         ")
                .withLayer("         ")
                .withLayer("    c    ")
                .map('i', ItemContext.of(new ItemBuilder(Material.ENDER_PORTAL_FRAME, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§aInfo Menu")
                        .addLore(" ", "§7Click a button below to view", "§7more information!")
                        .build())
                )
                .map('s', ItemContext.of(new ItemBuilder(Material.SAPLING, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§aSkill Tree")
                        .setData((short) 5)
                        .addLore(" ", "§7Click to view your §aSkill", "§aTree§7!")
                        .build())
                )
                .map('c', ItemContext.of(new ItemBuilder(Material.LONG_GRASS, ItemBuilder.ItemType.NORMAL)
                        .setDisplayName("§cClose")
                        .setData((short) 2)
                        .addLore(" ", "§7Click to close!")
                        .build())
                )
                .apply();
    }

    @Override
    public String getId() {
        return "const_info";
    }

    @Override
    public String getName(Locale locale) {
        return "Info";
    }

    @Override
    public List<String> getLore() {
        return LORE;
    }

    @Override
    public ItemStack getNewItemInstance() {
        return new ItemStack(Material.ENDER_PORTAL_FRAME);
    }
}
