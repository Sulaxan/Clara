package me.encast.clara.util.inventory.impl;

import me.encast.clara.util.inventory.AbstractInventory;
import me.encast.clara.util.inventory.ClickData;
import me.encast.clara.util.inventory.InvSize;
import org.bukkit.entity.Player;

/**
 * Basic implementation of {@link AbstractInventory}.
 */
public class EmptyInventory extends AbstractInventory {

    public EmptyInventory(String name, InvSize invSize) {
        super(name, invSize);
    }

    @Override
    public void onOpen(Player player) {
    }

    @Override
    public void onClick(ClickData data) {
    }

    @Override
    public void onClose(Player player) {
    }
}
