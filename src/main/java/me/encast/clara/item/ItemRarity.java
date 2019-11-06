package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
@Getter
public enum ItemRarity {

    STANDARD("Standard", "§a§lSTANDARD", ChatColor.GREEN),
    REFINED("Refined", "§9§lREFINED", ChatColor.BLUE),
    ANGELIC("Angelic", "§f§lANGELIC", ChatColor.WHITE),
    GODLY("Godly", "§6§lGODLY", ChatColor.GOLD),
    CORRUPTED("Corrupted", "§8§lCORRUPTED", ChatColor.DARK_GRAY);

    private String name;
    private String display;
    private ChatColor color;
}
