package me.encast.clara;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.encast.clara.command.ItemCommand;
import me.encast.clara.item.ItemManager;
import me.encast.clara.player.ClaraPlayerManager;
import me.encast.clara.util.event.ArmorListener;
import me.encast.clara.util.inventory.invx.InventoryManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Clara extends JavaPlugin {

    @Getter
    private static Clara instance;

    private ItemManager itemManager;
    private ClaraPlayerManager playerManager;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        //Sephrem.add();
//        Sephrem.registerEntity("Sephrem", 989, EntityPigZombie.class, Sephrem.class);

        // Armour listener should be registered before the other listeners
        getServer().getPluginManager().registerEvents(new ArmorListener(Lists.newArrayList()), this);

        // Managers
        this.itemManager = new ItemManager(this);
        this.playerManager = new ClaraPlayerManager();
        this.inventoryManager = new InventoryManager(this);

        getCommand("item").setExecutor(new ItemCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        inventoryManager.shutdown();
    }
}
