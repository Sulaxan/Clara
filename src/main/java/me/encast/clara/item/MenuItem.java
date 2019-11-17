package me.encast.clara.item;

import me.encast.clara.Clara;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.item.interact.InteractData;
import me.encast.clara.util.item.interact.InteractableItem;

public interface MenuItem extends ClaraItem, InteractableItem {

    UndefinedInv getInventory();

    @Override
    default ItemRarity getRarity() {
        return ItemRarity.NONE;
    }

    @Override
    default void interact(InteractData data) {
        data.setCancel(true);
        UndefinedInv inv = getInventory();
        if(inv != null)
            Clara.getInstance().getInventoryManager().openInv(data.getPlayer(), getInventory());
    }
}
