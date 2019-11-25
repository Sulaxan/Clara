package me.encast.clara.spell;

import me.encast.clara.player.ClaraPlayer;

public interface Spell {

    String getId();

    String getName();

    String getDescription();

    long getCooldown(); // in milliseconds

    void use(ClaraPlayer player);
}
