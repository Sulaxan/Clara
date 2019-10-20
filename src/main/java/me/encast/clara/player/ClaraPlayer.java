package me.encast.clara.player;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ClaraPlayer {

    private UUID uuid;

    public ClaraPlayer(UUID uuid) {
        this.uuid = uuid;
    }
}
