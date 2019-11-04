package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemRarity {

    STANDARD("Standard", "§a§lSTANDARD"),
    REFINED("Refined", "§9§lREFINED"),
    ANGELIC("Angelic", "§f§lANGELIC"),
    GODLY("Godly", "§6§lGODLY");

    private String name;
    private String display;
}
