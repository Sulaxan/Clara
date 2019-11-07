package me.encast.clara.player;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.RuntimeClaraItem;
import me.encast.clara.item.SaveableItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ClaraPlayer {

    @Setter(AccessLevel.NONE)
    private UUID uuid;

    private double health = 1000;
    private double defense = 0; // between 0 and 1

    private List<SaveableItem> item = Lists.newCopyOnWriteArrayList();

    // Runtime
    private transient List<RuntimeClaraItem> runtimeItems = Lists.newCopyOnWriteArrayList();
    private transient List<RuntimeClaraItem> equippedArmor = Lists.newCopyOnWriteArrayList();

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

    // returns whether it was equipped
    public boolean addEquippedArmor(RuntimeClaraItem item) {
        if(item.getItem() instanceof ClaraArmor) {
            ((ClaraArmor) item.getItem()).apply(this);
            return this.equippedArmor.add(item);
        }

        return false;
    }

    public void removeEquippedArmor(RuntimeClaraItem item) {
        if(item.getItem() instanceof ClaraArmor && this.equippedArmor.contains(item)) {
            ((ClaraArmor) item.getItem()).unapply(this);
            this.equippedArmor.remove(item);
        }
    }

    public void applyAllArmorBuffs() {
        equippedArmor.forEach(armor -> ((ClaraArmor) armor).apply(this));
    }

    public void unapplyAllArmorBuffs() {
        equippedArmor.forEach(armor -> ((ClaraArmor) armor).unapply(this));
    }

    public Player getBukkitPlayer() {
        return Bukkit.getServer().getPlayer(uuid);
    }
}
