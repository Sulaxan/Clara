package me.encast.clara.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ClaraSavePlayer {

    private UUID uuid;
    private double health;
    private double defense;

    private List<NBTTagCompound> items;
}
