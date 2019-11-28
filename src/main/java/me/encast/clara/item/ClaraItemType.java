package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.armor.impl.CrysalisArmorPiece;
import org.bukkit.Material;

import java.util.concurrent.Callable;

@AllArgsConstructor
public enum ClaraItemType {

    CRYSALIS_HELMET(CrysalisArmorPiece.HELMET_ID, () -> new CrysalisArmorPiece(
            ClaraArmor.Type.HELMET,
            Material.LEATHER_HELMET,
            CrysalisArmorPiece.HELMET_ID
    )),
    CRYSALIS_CHESTPLATE(CrysalisArmorPiece.CHESTPLATE_ID, () -> new CrysalisArmorPiece(
            ClaraArmor.Type.CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            CrysalisArmorPiece.CHESTPLATE_ID
    )),
    CRYSALIS_LEGGINGS(CrysalisArmorPiece.LEGGINGS_ID, () -> new CrysalisArmorPiece(
            ClaraArmor.Type.LEGGINGS,
            Material.LEATHER_LEGGINGS,
            CrysalisArmorPiece.LEGGINGS_ID
    )),
    CRYSALIS_BOOTS(CrysalisArmorPiece.BOOTS_ID, () -> new CrysalisArmorPiece(
            ClaraArmor.Type.BOOTS,
            Material.LEATHER_BOOTS,
            CrysalisArmorPiece.BOOTS_ID
    ));

    public static final ClaraItemType[] VALUES = values();

    @Getter
    private String id;
    private Callable<ClaraItem> constructCallable;

    public ClaraItem getItem() {
        try {
            return constructCallable.call();
        } catch (Exception e) {
            return null;
        }
    }
}
