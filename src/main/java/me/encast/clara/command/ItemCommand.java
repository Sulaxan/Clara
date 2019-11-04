package me.encast.clara.command;

import me.encast.clara.Clara;
import me.encast.clara.item.ClaraItemType;
import me.encast.clara.player.ClaraPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            ClaraPlayer cp = Clara.getInstance().getPlayerManager().getPlayer(p.getUniqueId());
            if(cp == null)
                Clara.getInstance().getPlayerManager().addPlayer(cp = new ClaraPlayer(p.getUniqueId()));

            if(args.length >= 1) {
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
