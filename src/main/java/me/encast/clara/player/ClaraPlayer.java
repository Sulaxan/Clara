package me.encast.clara.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.encast.clara.item.RuntimeClaraItem;
import me.encast.clara.item.SaveableItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ClaraPlayer {

    @Setter(AccessLevel.NONE)
    private UUID uuid;

    private double health = 1000;
    private double defense = 0; // between 0 and 1

    private Map<UUID, SaveableItem> items = Maps.newConcurrentMap();

    // Runtime
    private transient List<RuntimeClaraItem> runtimeItems = Lists.newCopyOnWriteArrayList();

    public ClaraPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void addHealth(double health) {
        this.health += health;
    }

    public void addDefense(double defense) {
        this.defense += defense;
    }

    public void addRuntimeItem(RuntimeClaraItem item) {
        this.runtimeItems.add(item);
    }

    public void removeRuntimeItem(RuntimeClaraItem item) {
        this.runtimeItems.remove(item);
    }

    public Player getBukkitPlayer() {
        return Bukkit.getServer().getPlayer(uuid);
    }
}
