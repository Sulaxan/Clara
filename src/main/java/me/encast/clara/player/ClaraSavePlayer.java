package me.encast.clara.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.encast.clara.util.nbt.SaveableNBT;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ClaraSavePlayer {

    private UUID uuid;
    private double health;
    private double defense;

    private List<SaveableNBT> items;
}
