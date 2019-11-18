package me.encast.clara.item;

import me.encast.clara.Clara;
import me.encast.clara.util.inventory.invx.UndefinedInv;
import me.encast.clara.util.item.interact.InteractData;
import me.encast.clara.util.item.interact.InteractableItem;
import org.bukkit.entity.Player;

import java.util.Locale;

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
            inv.setName(getName(Locale.ENGLISH));
            Clara.getInstance().getInventoryManager().openInv(data.getPlayer(), inv);
        }
    }
}
