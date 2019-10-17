package me.encast.clara.util.hologram;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.encast.clara.util.entity.WrappedArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public abstract class Hologram {

    private Location location;
    private double spacing;

    private List<WrappedArmorStand> holograms = Lists.newCopyOnWriteArrayList();

    public Hologram(Location location) {
        this(location, 0.25);
    }

    public Hologram(Location location, double spacing) {
        this.location = location;
        this.spacing = spacing;
    }

    public void move(Location location) {
        this.location = location;
        retranslate();
    }

    public void addText(String text) {
        addText(text, holograms.size());
    }

    public void addText(String text, int line) {
        if(line <= holograms.size()) {
            WrappedArmorStand armorStand = new WrappedArmorStand(location);
            armorStand.getEntity().setBasePlate(false);
            armorStand.getEntity().setGravity(false);
            armorStand.getEntity().setInvisible(true);
            armorStand.setCustomName(text);
            armorStand.getEntity().setCustomNameVisible(true);
            holograms.add(line, armorStand);
            retranslate();
        }
    }

    public void setText(int line, String text) {
        if(line >= 0 && line < holograms.size()) {
            WrappedArmorStand was = getHolograms().get(line);
            was.setCustomName(text);
        } else {
            for(int i = 0; i < line - holograms.size() + 1; i++) {
                addText("");
            }
            // Call method again, this code won't get called against since the first if condition
            // is satisfied
            setText(line, text);
        }
    }

    // Lines start at 0
    public void removeLine(int line) {
        if(line < holograms.size())
            removeHologram(holograms.remove(line));
    }

    public void clear() {
        holograms.forEach(this::removeHologram);
        holograms.clear();
    }

    public void show(Player p) {
        for(WrappedArmorStand as : holograms) {
            as.show(p);
        }
    }

    public void showAll() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::show);
    }

    public void hide(Player p) {
        for(WrappedArmorStand as : holograms) {
            as.despawn(p);
        }
    }

    public void hideAll() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::hide);
    }

    public void hideAndDestroy() {
        hideAll();
        clear();
    }

    private void retranslate() {
        // 2 - spacing since armor stands are 2 blocks high
        Location base = location.clone().subtract(0, 2 - spacing, 0);
        for(int i = holograms.size() - 1; i >= 0; i--) {
            WrappedArmorStand as = holograms.get(i);
            as.teleport(base);
            if(as.getCustomName().isEmpty()) {
                as.despawnAll();
            }
            base.add(0, spacing, 0);
        }
    }

    private void removeHologram(WrappedArmorStand armorStand) {
        armorStand.getShownPlayers().forEach(uuid -> {
            Player player = Bukkit.getServer().getPlayer(uuid);
            if(player != null)
                armorStand.despawn(player);
        });
    }
}
