package me.encast.clara.item;

import me.encast.clara.Clara;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.item.interact.InteractData;
import me.encast.clara.util.item.interact.InteractableItem;
import me.encast.clara.util.resource.Locale;
import org.bukkit.entity.Player;

public interface MenuItem extends ClaraItem, InteractableItem {

    UndefinedInv getInventory(Player player);

    @Override
    default ItemRarity getRarity() {
        return ItemRarity.NONE;
    }

    @Override
    default void interact(InteractData data) {
        data.setCancel(true);
        UndefinedInv inv = getInventory(data.getPlayer());
        if(inv != null) {
            inv.setName(getName(Locale.EN_US));
            Clara.getInstance().getInventoryManager().openInv(data.getPlayer(), inv);
        }
    }
}
