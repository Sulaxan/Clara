package me.encast.clara.player;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.encast.clara.item.SaveableItem;

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

    public ClaraPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void addHealth(double health) {
        this.health += health;
    }

    public void addDefense(double defense) {
        this.defense += defense;
    }
}
