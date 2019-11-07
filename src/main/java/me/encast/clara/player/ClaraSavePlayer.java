package me.encast.clara.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.encast.clara.item.SaveableItem;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ClaraSavePlayer {

    private UUID uuid;
    private double health;
    private double defense;

    private List<SaveableItem> items;
}
