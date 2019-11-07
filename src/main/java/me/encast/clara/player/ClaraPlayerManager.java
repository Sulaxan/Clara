package me.encast.clara.player;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import me.encast.clara.item.ItemManager;
import me.encast.clara.item.RuntimeClaraItem;
import me.encast.clara.item.SaveableItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ClaraPlayerManager {

    private List<ClaraPlayer> players = Lists.newArrayList();

    public ClaraPlayerManager() {
    }

    public ClaraPlayer getPlayer(UUID uuid) {
        for(ClaraPlayer player : players) {
            if(player.getUuid().equals(uuid))
                return player;
        }
        return null;
    }

    public void addPlayer(ClaraPlayer player) {
        this.players.add(player);
    }

    public ClaraSavePlayer savePlayer(ClaraPlayer player) {
        Player p = player.getBukkitPlayer();
        ItemManager manager = Clara.getInstance().getItemManager();
        List<SaveableItem> items = Lists.newArrayList();
        int slot = 0;
        for(ItemStack item : p.getInventory().getContents()) {
            if(item != null) {
                RuntimeClaraItem runtime = manager.getRuntimeItem(player, item);
                if (runtime != null) {
                    // Check to make sure item count does not go above the runtime count by using a map

                    items.add(new SaveableItem(slot, manager.saveItem(item, runtime)));
                }
            }
            slot++;
        }

        slot = 100; // boots slot
        // Goes from boots to helmet
//        for(ItemStack item : p.getInventory().getArmorContents()) {
//            if(item != null) {
//                RuntimeClaraItem runtime = manager.getRuntimeItem(player, item);
//                if (runtime != null) {
//                    // Check to make sure item count does not go above the runtime count by using a map
//
//                    items.add(new SaveableItem(slot, manager.saveItem(item, runtime)));
//                }
//            }
//            slot++;
//        }

        player.unapplyAllArmorBuffs();
        ClaraSavePlayer save = new ClaraSavePlayer(player.getUuid(), player.getHealth(), player.getDefense(), items);
        player.applyAllArmorBuffs();
        return save;
    }
}
