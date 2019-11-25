package me.encast.clara.skilltree;

import me.encast.clara.player.ClaraPlayer;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TreeComponent {

    private List<String> lore;
    private Consumer<ClaraPlayer> apply;

    public TreeComponent(Consumer<ClaraPlayer> apply) {
        this(Collections.emptyList(), apply);
    }

    public TreeComponent(List<String> lore, Consumer<ClaraPlayer> apply) {
        this.lore = lore;
        this.apply = apply;
    }
}
