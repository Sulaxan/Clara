package me.encast.clara.entity;

import me.encast.clara.util.hologram.Hologram;
import net.minecraft.server.v1_8_R3.EntityLiving;

public interface ClaraEntity {

    String getName();

    ClaraEntitySubType getSubType();

    int getCurrentTick();

    Hologram getHologram();

    EntityLiving getEntity();
}
