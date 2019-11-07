package me.encast.clara.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.encast.clara.Clara;
import me.encast.clara.item.ClaraItemType;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.player.ClaraSavePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

public class ItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            ClaraPlayer cp = Clara.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
            if(cp == null)
                Clara.getInstance().getPlayerManager().addPlayer(cp = new ClaraPlayer(p.getUniqueId()));

            if(args.length >= 1) {
                if(args[0].equalsIgnoreCase("save")) {
                    ClaraSavePlayer save = Clara.getInstance().getPlayerManager().savePlayer(cp);
                    try {
                        File dir = Clara.getInstance().getDataFolder();
                        if(!dir.exists())
                            dir.mkdir();

                        String uuid = UUID.randomUUID().toString().split("-")[4];

                        File saveFile = new File(dir.getAbsolutePath() + File.separatorChar + uuid);
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        FileWriter writer = new FileWriter(saveFile);
                        gson.toJson(save, writer);
                        writer.close();
                        p.sendMessage("§aCreated snapshot save with id " + uuid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                p.sendMessage("§aRUNTIME ITEMS: " + cp.getRuntimeItems().size());
                return true;
            }

            ItemStack item = Clara.getInstance().getItemManager()
                    .constructNewItem(cp, ClaraItemType.CRYSALIS_HELMET.getItem(), true);
            p.getInventory().addItem(item);
            p.sendMessage("§aGiven!");
        }
        return true;
    }
}
