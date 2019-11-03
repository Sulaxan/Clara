package me.encast.clara.player;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

public class ClaraPlayerManager {

    private List<ClaraPlayer> players = Lists.newArrayList();

    public ClaraPlayerManager() {
    }

    public ClaraPlayer getPlayer(UUID uuid) {
        for(ClaraPlayer player : players) {
            if(player.getUuid().equals(uuid))
                return player;
        }
        return null;
    }

    public void addPlayer(ClaraPlayer player) {
        this.players.add(player);
    }
}
