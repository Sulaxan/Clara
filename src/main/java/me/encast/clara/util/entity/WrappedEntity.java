package me.encast.clara.util.entity;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Getter
public abstract class WrappedEntity<T extends Entity> {

    private Set<UUID> shownPlayers = Sets.newConcurrentHashSet();

    public WrappedEntity() {
//        getEntity().valid = true;
    }

    public final Set<UUID> getShownPlayers() {
        return shownPlayers;
    }

    public String getCustomName() {
        return getEntity().getCustomName();
    }

    public void setCustomName(String name) {
        getEntity().setCustomName(name);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(getEntity().getId(), getEntity().getDataWatcher(), false);
        shownPlayers.forEach(uuid -> {
            Player p = Bukkit.getServer().getPlayer(uuid);
            if(p != null)
                sendPacket(p, metadata);
        });
    }

    public void teleport(Location location) {
        getEntity().setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(getEntity());

        getShownPlayers().forEach(uuid -> {
            Player p = Bukkit.getServer().getPlayer(uuid);
            if(p != null)
                sendPacket(p, packet);
        });
    }

    public void show(Player player) {
        show(player, false);
    }

    public void show(Player player, boolean force) {
        if(shownPlayers.contains(player.getUniqueId()) && !force)
            return;
        Packet spawn;
        if(getEntity() instanceof EntityLiving) {
            spawn = new PacketPlayOutSpawnEntityLiving((EntityLiving) getEntity());
        } else {
            spawn = new PacketPlayOutSpawnEntity(getEntity(), 0);
        }
        sendPacket(player, spawn);
        shownPlayers.add(player.getUniqueId());
    }

    public void despawn(Player player) {
        if(shownPlayers.remove(player.getUniqueId())) {
            PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(getEntity().getId());
            sendPacket(player, destroy);
        }
    }

    public void despawnAll() {
        Bukkit.getServer().getOnlinePlayers().forEach(this::despawn);
    }
    
    public abstract T getEntity();

    private void sendPacket(Player player, Packet... packets) {
        if(packets != null && packets.length >= 1)
            Arrays.stream(packets).forEach(packet ->
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet));
    }
}
