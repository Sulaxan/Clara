package me.encast.clara.player;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.encast.clara.Clara;
import me.encast.clara.item.ClaraItem;
import me.encast.clara.item.ItemManager;
import me.encast.clara.item.MenuItem;
import me.encast.clara.item.RuntimeClaraItem;
import me.encast.clara.item.impl.DropItem;
import me.encast.clara.item.impl.ProfileItem;
import me.encast.clara.item.impl.SkillTreeItem;
import me.encast.clara.item.impl.SpellItem;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

@Getter
public class ClaraPlayerManager implements Listener {

    private List<ClaraPlayer> players = Lists.newArrayList();
    private WeakHashMap<UUID, ClaraSavePlayer> saveCache = new WeakHashMap<>();
    private File saveDirectory;

    public ClaraPlayerManager(Plugin plugin) {
        this.saveDirectory = new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "player_saves");
        if(!this.saveDirectory.exists())
            this.saveDirectory.mkdir();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
                if (runtime != null && !(runtime.getItem() instanceof MenuItem)) {
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

    public ClaraPlayer loadFromSaveDir(Player p) {
        // Try to load from cache first
        ClaraSavePlayer savePlayer = getSaveCache().getOrDefault(p.getUniqueId(), null);
        if(savePlayer == null) {
            File save = new File(getSaveDirectory().getAbsolutePath() + File.separatorChar + p.getUniqueId().toString() + ".json");
            if(save.exists()) {
                try (FileReader reader = new FileReader(save)) {
                    savePlayer = Clara.GSON.fromJson(reader, ClaraSavePlayer.class);
                    saveCache.put(p.getUniqueId(), savePlayer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(savePlayer != null) {
            return loadPlayer(p, savePlayer);
        }
        return null;
    }

    public void saveToFile(ClaraSavePlayer savePlayer) {
        File save = new File(getSaveDirectory().getAbsolutePath() + File.separatorChar + savePlayer.getUuid().toString() + ".json");
        if(save.exists()) {
           save.delete();
        }

        try (FileWriter writer = new FileWriter(save)) {
            Clara.PRETTY_GSON.toJson(savePlayer, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyMenuIcons(ClaraPlayer player) {
        Player p = player.getBukkitPlayer();
        ItemStack i = Clara.getInstance().getItemManager().constructNewItem(player, new SkillTreeItem(), true);
        p.getInventory().setItem(17, i);
        i = Clara.getInstance().getItemManager().constructNewItem(player, new DropItem(), true);
        p.getInventory().setItem(26, i);
        i = Clara.getInstance().getItemManager().constructNewItem(player, new ProfileItem(), true);
        p.getInventory().setItem(27, i);
        i = Clara.getInstance().getItemManager().constructNewItem(player, new SpellItem(), true);
        p.getInventory().setItem(35, i);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ClaraPlayer player = loadFromSaveDir(p);
        if(player == null) {
            // New player
            player = new ClaraPlayer(p.getUniqueId());
        }

        // Add to current runtime players
        players.add(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        ClaraPlayer player = getPlayer(p.getUniqueId());
        if(player != null) {
            // Remove from runtime players
            players.remove(player);
            ClaraSavePlayer savePlayer = savePlayer(player);
            if(savePlayer != null) { // Just in case
                saveCache.put(p.getUniqueId(), savePlayer);
                saveToFile(savePlayer);
            }
        }
    }
}
