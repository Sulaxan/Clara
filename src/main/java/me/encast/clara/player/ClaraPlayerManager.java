package me.encast.clara.player;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.Getter;
import me.encast.clara.Clara;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.item.ItemManager;
import me.encast.clara.item.RuntimeClaraItem;
import me.encast.clara.util.nbt.SaveableNBT;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@Getter
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
        List<NBTTagCompound> items = Lists.newArrayList();
        int slot = 0;

        NBTTagCompound compound;
        for(ItemStack item : p.getInventory().getContents()) {
            if(item != null) {
                RuntimeClaraItem runtime = manager.getRuntimeItem(player, item);
                if (runtime != null) {
                    // Check to make sure item count does not go above the runtime count by using a map
                    compound = manager.saveItem(item, runtime);
                    compound.setInt(ClaraItem.SLOT_KEY, slot);
                    items.add(compound);
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

    public ClaraPlayer loadPlayer(Player player, ClaraSavePlayer save) {
        ClaraPlayer cp = new ClaraPlayer(player.getUniqueId());

        player.getInventory().clear();

        ItemManager manager = Clara.getInstance().getItemManager();

        for(NBTTagCompound tag : save.getItems()) {
            if(tag.hasKey(ClaraItem.SLOT_KEY)) {
                int slot = tag.getInt(ClaraItem.SLOT_KEY);
                if(slot < 100) {
                    // Regular inventory item
                    manager.load(cp, tag, true);
                } else {
                    // Armour
                }
            }
        }

        player.updateInventory();

        return cp;
    }
}
