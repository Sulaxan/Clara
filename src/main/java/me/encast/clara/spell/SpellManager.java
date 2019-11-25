package me.encast.clara.spell;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Consumer;

public class SpellManager {

    private Plugin plugin;
    // Singleton instances of every spell
    private Map<String, Spell> registeredSpells = Maps.newConcurrentMap();
    private Map<UUID, List<SpellCooldown>> cooldowns = Maps.newConcurrentMap();

    // Invalidating any players without any cooldowns
    private final Consumer<UUID> GLOBAL_CALLBACK = uuid -> {
        List<SpellCooldown> cooldowns = this.cooldowns.getOrDefault(uuid, null);
        if(cooldowns != null && cooldowns.isEmpty())
            this.cooldowns.remove(uuid);
    };

    public SpellManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerSpell(Spell spell) {
        this.registeredSpells.put(spell.getId(), spell);
    }

    public void registerSpells(Spell... spells) {
        if(spells != null && spells.length >= 1) {
            Arrays.stream(spells)
                    .filter(Objects::nonNull)
                    .forEach(this::registerSpell);
        }
    }

    public Spell getSpell(String id) {
        return registeredSpells.getOrDefault(id, null);
    }

    public SpellCooldown getCooldown(UUID uuid, Spell spell) {
        List<SpellCooldown> cooldowns = this.cooldowns.getOrDefault(uuid, null);
        if(cooldowns != null) {
            for(SpellCooldown cooldown : cooldowns) {
                if(cooldown.getSpellId().equals(spell.getId())) {
                    return cooldown;
                }
            }
        }

        return null;
    }

    public void addCooldown(UUID uuid, Spell spell) {
        List<SpellCooldown> cooldowns = this.cooldowns.getOrDefault(uuid, Lists.newArrayList());
        if(cooldowns.isEmpty())
            this.cooldowns.put(uuid, cooldowns);
        SpellCooldown cooldown = new SpellCooldown(
                uuid,
                spell.getId(),
                System.currentTimeMillis(),
                cooldowns,
                GLOBAL_CALLBACK

        );
        cooldowns.add(cooldown);
        cooldown.runTaskLater(plugin, spell.getCooldown() / 50);
    }

    @Getter
    public class SpellCooldown extends BukkitRunnable {

        private UUID uuid;
        private String spellId;
        private long startTime;
        private List<SpellCooldown> cooldowns;
        private Consumer<UUID> callback;

        public SpellCooldown(UUID uuid, String spellId, long startTime, List<SpellCooldown> cooldowns, Consumer<UUID> callback) {
            this.uuid = uuid;
            this.spellId = spellId;
            this.startTime = startTime;
            this.cooldowns = cooldowns;
            this.callback = callback;
        }

        @Override
        public void run() {
            cooldowns.remove(this);
        }
    }
}
