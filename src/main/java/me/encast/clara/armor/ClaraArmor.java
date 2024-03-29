package me.encast.clara.armor;

import me.encast.clara.item.ClaraItem;
import me.encast.clara.item.ItemCategory;
import me.encast.clara.player.ClaraPlayer;

public interface ClaraArmor extends ClaraItem {

    void apply(ClaraPlayer player);

    void unapply(ClaraPlayer player);

    Type getType();

    @Override
    default ItemCategory getCategory() {
        return ItemCategory.ARMOR;
    }

    @Override
    default int getAmount() {
        return 1;
    }

    @Override
    default void setAmount(int amount) {
    }

    @Override
    default short getDurability() {
        return 255;
    }

    @Override
    default void setDurability(short durability) {
    }

    enum Type {

        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }
}
