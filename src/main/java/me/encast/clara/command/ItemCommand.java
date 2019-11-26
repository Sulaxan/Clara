package me.encast.clara.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.encast.clara.Clara;
import me.encast.clara.item.ClaraItemType;
import me.encast.clara.player.ClaraPlayer;
import me.encast.clara.player.ClaraSavePlayer;
import me.encast.clara.util.item.ItemUtil;
import me.encast.clara.util.nbt.NBTTagCompoundSerializer;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
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
                if(args[0].equalsIgnoreCase("apply")) {
                    Clara.getInstance().getPlayerManager().applyMenuIcons(cp);
                }
                if(args[0].equalsIgnoreCase("nbt")) {
                    if(p.getItemInHand() != null) {
                        Bukkit.broadcastMessage(new GsonBuilder()
                                .create().toJson(ItemUtil.getRawNBT(p.getItemInHand())));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("load")) {
                    if(args.length >= 2) {
                        File dir = Clara.getInstance().getDataFolder();
                        if(!dir.exists())
                            dir.mkdir();

                        File saveFile = new File(dir.getAbsolutePath() + File.separatorChar + args[1]);
                        if(saveFile.exists()) {
                            try {
                                FileReader reader = new FileReader(saveFile);
                                ClaraPlayer cpTemp = Clara.getInstance().getPlayerManager().loadPlayer(p,
                                        new GsonBuilder()
                                                .registerTypeAdapter(NBTBase.class, new NBTTagCompoundSerializer())
                                                .create()
                                                .fromJson(reader, ClaraSavePlayer.class));
                                reader.close();
                                Clara.getInstance().getPlayerManager().getPlayers().remove(cp);
                                Clara.getInstance().getPlayerManager().addPlayer(cpTemp);
                                p.sendMessage("§aLoaded");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            p.sendMessage("§cSave does not exist");
                        }
                    } else {
                        p.sendMessage("§cSpecify a save file");
                    }
                    return true;
                }
                if(args[0].equalsIgnoreCase("save")) {
                    ClaraSavePlayer save = Clara.getInstance().getPlayerManager().savePlayer(cp);
                    try {
                        File dir = Clara.getInstance().getDataFolder();
                        if(!dir.exists())
                            dir.mkdir();

                        String uuid = UUID.randomUUID().toString().split("-")[4];

                        File saveFile = new File(dir.getAbsolutePath() + File.separatorChar + uuid);
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(NBTBase.class, new NBTTagCompoundSerializer())
                                .setPrettyPrinting()
                                .create();
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
                p.sendMessage("RESOURCE MESSAGE:");
                p.sendMessage(Clara.GENERAL_MSG.getAndFormat("en_US", "command.item.runtime_items", cp.getRuntimeItems().size()));
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
