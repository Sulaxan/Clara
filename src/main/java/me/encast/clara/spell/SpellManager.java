package me.encast.clara.spell;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class SpellManager {

    // Singleton instances of every spell
    private Map<String, Spell> registeredSpells = Maps.newConcurrentMap();

    public SpellManager() {
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
}
