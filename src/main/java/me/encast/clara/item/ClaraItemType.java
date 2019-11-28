package me.encast.clara.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.armor.impl.*;
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
    )),

    SAPHRON_HELMET(SaphronArmorPiece.HELMET_ID, () -> new SaphronArmorPiece(
            ClaraArmor.Type.HELMET,
            Material.LEATHER_HELMET,
            SaphronArmorPiece.HELMET_ID
    )),
    SAPHRON_CHESTPLATE(SaphronArmorPiece.CHESTPLATE_ID, () -> new SaphronArmorPiece(
            ClaraArmor.Type.CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            SaphronArmorPiece.CHESTPLATE_ID
    )),
    SAPHRON_LEGGINGS(SaphronArmorPiece.LEGGINGS_ID, () -> new SaphronArmorPiece(
            ClaraArmor.Type.LEGGINGS,
            Material.LEATHER_LEGGINGS,
            SaphronArmorPiece.LEGGINGS_ID
    )),
    SAPHRON_BOOTS(SaphronArmorPiece.BOOTS_ID, () -> new SaphronArmorPiece(
            ClaraArmor.Type.BOOTS,
            Material.LEATHER_BOOTS,
            SaphronArmorPiece.BOOTS_ID
    )),

    SOLIDIFIED_DAUNA_HELMET(SolidifiedDaunaArmorPiece.HELMET_ID, () -> new SolidifiedDaunaArmorPiece(
            ClaraArmor.Type.HELMET,
            Material.LEATHER_HELMET,
            SolidifiedDaunaArmorPiece.HELMET_ID
    )),
    SOLIDIFIED_DAUNA_CHESTPLATE(SolidifiedDaunaArmorPiece.CHESTPLATE_ID, () -> new SolidifiedDaunaArmorPiece(
            ClaraArmor.Type.CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            SolidifiedDaunaArmorPiece.CHESTPLATE_ID
    )),
    SOLIDIFIED_DAUNA_LEGGINGS(SolidifiedDaunaArmorPiece.LEGGINGS_ID, () -> new SolidifiedDaunaArmorPiece(
            ClaraArmor.Type.LEGGINGS,
            Material.LEATHER_LEGGINGS,
            SolidifiedDaunaArmorPiece.LEGGINGS_ID
    )),
    SOLIDIFIED_DAUNA_BOOTS(SolidifiedDaunaArmorPiece.BOOTS_ID, () -> new SolidifiedDaunaArmorPiece(
            ClaraArmor.Type.BOOTS,
            Material.LEATHER_BOOTS,
            SolidifiedDaunaArmorPiece.BOOTS_ID
    )),

    MOONSHADOW_HELMET(MoonshadowArmorPiece.HELMET_ID, () -> new MoonshadowArmorPiece(
            ClaraArmor.Type.HELMET,
            Material.LEATHER_HELMET,
            MoonshadowArmorPiece.HELMET_ID
    )),
    MOONSHADOW_CHESTPLATE(MoonshadowArmorPiece.CHESTPLATE_ID, () -> new MoonshadowArmorPiece(
            ClaraArmor.Type.CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            MoonshadowArmorPiece.CHESTPLATE_ID
    )),
    MOONSHADOW_LEGGINGS(MoonshadowArmorPiece.LEGGINGS_ID, () -> new MoonshadowArmorPiece(
            ClaraArmor.Type.LEGGINGS,
            Material.LEATHER_LEGGINGS,
            MoonshadowArmorPiece.LEGGINGS_ID
    )),
    MOONSHADOW_BOOTS(MoonshadowArmorPiece.BOOTS_ID, () -> new MoonshadowArmorPiece(
            ClaraArmor.Type.BOOTS,
            Material.LEATHER_BOOTS,
            MoonshadowArmorPiece.BOOTS_ID
    )),

    DEMON_HELMET(DemonArmorPiece.HELMET_ID, () -> new DemonArmorPiece(
            ClaraArmor.Type.HELMET,
            Material.LEATHER_HELMET,
            DemonArmorPiece.HELMET_ID
    )),
    DEMON_CHESTPLATE(DemonArmorPiece.CHESTPLATE_ID, () -> new DemonArmorPiece(
            ClaraArmor.Type.CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            DemonArmorPiece.CHESTPLATE_ID
    )),
    DEMON_LEGGINGS(DemonArmorPiece.LEGGINGS_ID, () -> new DemonArmorPiece(
            ClaraArmor.Type.LEGGINGS,
            Material.LEATHER_LEGGINGS,
            DemonArmorPiece.LEGGINGS_ID
    )),
    DEMON_BOOTS(DemonArmorPiece.BOOTS_ID, () -> new DemonArmorPiece(
            ClaraArmor.Type.BOOTS,
            Material.LEATHER_BOOTS,
            DemonArmorPiece.BOOTS_ID
    )),

    MUTATED_DEMON_HELMET(MutatedDemonArmorPiece.HELMET_ID, () -> new MutatedDemonArmorPiece(
            ClaraArmor.Type.HELMET,
            Material.LEATHER_HELMET,
            DemonArmorPiece.HELMET_ID
    )),
    MUTATED_DEMON_CHESTPLATE(MutatedDemonArmorPiece.CHESTPLATE_ID, () -> new MutatedDemonArmorPiece(
            ClaraArmor.Type.CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            MutatedDemonArmorPiece.CHESTPLATE_ID
    )),
    MUTATED_DEMON_LEGGINGS(MutatedDemonArmorPiece.LEGGINGS_ID, () -> new MutatedDemonArmorPiece(
            ClaraArmor.Type.LEGGINGS,
            Material.LEATHER_LEGGINGS,
            MutatedDemonArmorPiece.LEGGINGS_ID
    )),
    MUTATED_DEMON_BOOTS(MutatedDemonArmorPiece.BOOTS_ID, () -> new MutatedDemonArmorPiece(
            ClaraArmor.Type.BOOTS,
            Material.LEATHER_BOOTS,
            MutatedDemonArmorPiece.BOOTS_ID
    )),

    TIME_CRYSTAL_HELMET(TimeCrystalArmorPiece.HELMET_ID, () -> new TimeCrystalArmorPiece(
            ClaraArmor.Type.HELMET,
            Material.LEATHER_HELMET,
            TimeCrystalArmorPiece.HELMET_ID
    )),
    TIME_CRYSTAL_CHESTPLATE(TimeCrystalArmorPiece.CHESTPLATE_ID, () -> new TimeCrystalArmorPiece(
            ClaraArmor.Type.CHESTPLATE,
            Material.LEATHER_CHESTPLATE,
            TimeCrystalArmorPiece.CHESTPLATE_ID
    )),
    TIME_CRYSTAL_LEGGINGS(TimeCrystalArmorPiece.LEGGINGS_ID, () -> new TimeCrystalArmorPiece(
            ClaraArmor.Type.LEGGINGS,
            Material.LEATHER_LEGGINGS,
            TimeCrystalArmorPiece.LEGGINGS_ID
    )),
    TIME_CRYSTAL_BOOTS(TimeCrystalArmorPiece.BOOTS_ID, () -> new TimeCrystalArmorPiece(
            ClaraArmor.Type.BOOTS,
            Material.LEATHER_BOOTS,
            TimeCrystalArmorPiece.BOOTS_ID
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
