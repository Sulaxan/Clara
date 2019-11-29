package me.encast.clara.command;

import com.google.common.collect.Lists;
import me.encast.clara.Clara;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.UUID;

public class RtMemoryFootprintCommand implements CommandExecutor {

    private List<UUID> players = Lists.newArrayList();
    private BukkitTask task;

    public RtMemoryFootprintCommand() {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                String message = "§aMemory Footprint: §c" +
                        ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory()) / 1000000) +
                        " MB §a/§4" +
                        ((Runtime.getRuntime().maxMemory()) / 1000000) +
                        " MB";

                for(UUID uuid : players) {
                    Player p = Bukkit.getServer().getPlayer(uuid);
                    if(p != null) {
                        sendActionBar(p, message);
                    }
                }
                message = null; // release reference for garbage collection
            }
        }.runTaskTimer(Clara.getInstance(), 0, 10);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(args.length >= 1) {
                if(args[0].equals("gc")) {
                    p.sendMessage("§7Calling garbage collector...");
                    System.gc();
                }
            } else {
                if (players.contains(p.getUniqueId())) {
                    players.remove(p.getUniqueId());
                    p.sendMessage("§cRemoved!");
                } else {
                    players.add(p.getUniqueId());
                    p.sendMessage("§aAdded!");
                }
            }
        } else {
            sender.sendMessage("Only players can use this command!");
        }
        return true;
    }

    private void sendActionBar(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
