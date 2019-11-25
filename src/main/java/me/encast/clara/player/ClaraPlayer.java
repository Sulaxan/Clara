package me.encast.clara.player;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.encast.clara.armor.ClaraArmor;
import me.encast.clara.item.RuntimeClaraItem;
import me.encast.clara.spell.Spell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ClaraPlayer {

    @Setter(AccessLevel.NONE)
    private UUID uuid;

    private double health = 1000;
    private double defense = 0; // between 0 and 1
    private int skillTokens = 0;

    private String[] equippedSpells = new String[0];
    private List<String> unlockedSpells = Lists.newCopyOnWriteArrayList();

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

    public ClaraArmor getEquippedArmor(ClaraArmor.Type type) {
        for(RuntimeClaraItem runtime : equippedArmor) {
            // Safe to cast since we ensure only ClaraArmor is in equippedArmor
            ClaraArmor armor = (ClaraArmor) runtime.getItem();
            if(armor.getType() == type)
                return armor;
        }
        return null;
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
        equippedArmor.forEach(armor -> ((ClaraArmor) armor.getItem()).apply(this));
    }

    public void unapplyAllArmorBuffs() {
        equippedArmor.forEach(armor -> ((ClaraArmor) armor.getItem()).unapply(this));
    }

    public boolean addSpell(Spell spell) {
        return unlockedSpells.add(spell.getId());
    }

    public boolean removeSpell(Spell spell) {
        return unlockedSpells.remove(spell.getId());
    }

    public boolean hasSpell(Spell spell) {
        return unlockedSpells.contains(spell.getId());
    }

    public String getEquippedSpell(int slot) {
        if(slot >= equippedSpells.length)
            throw new IndexOutOfBoundsException("Spell slot must be between 0 and " + (equippedSpells.length - 1));
        return equippedSpells[slot];
    }

    // spell can be null
    public void setEquippedSpell(int slot, @Nullable Spell spell) {
        if(slot >= equippedSpells.length)
            throw new IndexOutOfBoundsException("Spell slot must be between 0 and " + (equippedSpells.length - 1));
        equippedSpells[slot] = spell == null ? null : spell.getName();
    }

    public Player getBukkitPlayer() {
        return Bukkit.getServer().getPlayer(uuid);
    }
}
