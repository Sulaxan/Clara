package me.encast.clara.util.inventory.invx;

import org.bukkit.entity.Player;

public interface InteractableInv {

    void onOpen(Player player);

    void onClose(Player player);

    void onClick(ClickContext ctx);
}
