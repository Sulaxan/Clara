package me.encast.clara.command;

import me.encast.clara.world.ClaraGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WorldGenCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(args.length > 0) {
                String world = args[0];
                World w = Bukkit.getServer().getWorld(world);
                if(w != null) {
                    p.teleport(new Location(w, 0, 100, 0));
                    p.sendMessage("§aTeleported!");
                }
                return true;
            }
            String name = UUID.randomUUID().toString().split("-")[3];
            WorldCreator creator = new WorldCreator(name);
            creator.generator(new ClaraGenerator());
            creator.environment(World.Environment.NORMAL);
            World world = Bukkit.getServer().createWorld(creator);
            p.sendMessage("§aCreated Clara world with name: " + name);
            p.teleport(new Location(world, 0, 100, 0));
        }
        return true;
    }
}
