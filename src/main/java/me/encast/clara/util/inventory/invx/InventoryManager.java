package me.encast.clara.util.inventory.invx;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InventoryManager implements Listener {

    private Map<UUID, InvSession> openInvs = Maps.newConcurrentMap();

    public InventoryManager(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void shutdown() {
        for(InvSession session : openInvs.values()) {
            for(HumanEntity entity : session.getBukkitInv().getViewers()) {
                entity.closeInventory();
            }
        }
    }

    public UndefinedInv constructInv(BiConsumer<Player, InvSession> open, BiConsumer<Player, InvSession> close, Consumer<ClickContext> click) {
        return new GenericInventory(this, open, close, click, player -> openInvs.getOrDefault(player.getUniqueId(), null));
    }

    public void openInv(Player player, UndefinedInv inv) {
        if(!(inv instanceof ConstructableInv))
            throw new UnsupportedOperationException("Inventory must be constructable, implement ConstructableInv");
        if(inv instanceof InteractableInv)
            ((InteractableInv) inv).onOpen(player);
        openInvs.put(player.getUniqueId(), new InvSession(inv, ((ConstructableInv) inv).build()));
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        InvSession session = openInvs.remove(e.getPlayer().getUniqueId());
        if(session != null) {
            if(session.getUndefInv() instanceof InteractableInv)
                ((InteractableInv) session.getUndefInv()).onClose((Player) e.getPlayer());
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        InvSession session = openInvs.getOrDefault(e.getWhoClicked().getUniqueId(), null);
        if(session != null) {
            ClickContext ctx = new ClickContext(
                    (Player) e.getWhoClicked(),
                    session,
                    e.getCurrentItem(),
                    e.getSlot(),
                    e.getSlotType(),
                    e.getClick(),
                    e.getAction()
            );
            if(session.getUndefInv() instanceof InteractableInv)
                ((InteractableInv) session.getUndefInv()).onClick(ctx);

            e.setCancelled(ctx.isCancel());
        }
    }

    @AllArgsConstructor
    @Getter
    public class InvSession {

        private UndefinedInv undefInv;
        private Inventory bukkitInv;
    }
}
