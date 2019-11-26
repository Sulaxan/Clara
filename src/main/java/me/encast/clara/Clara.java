package me.encast.clara;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.encast.clara.command.ItemCommand;
import me.encast.clara.item.ItemManager;
import me.encast.clara.player.ClaraPlayerManager;
import me.encast.clara.util.event.ArmorListener;
import me.encast.clara.util.inventory.invx.InventoryManager;
import me.encast.clara.util.resource.JsonResourceCluster;
import me.encast.clara.util.resource.JsonResourceLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public final class Clara extends JavaPlugin {

    @Getter
    private static Clara instance;

    private ItemManager itemManager;
    private ClaraPlayerManager playerManager;
    private InventoryManager inventoryManager;

    public static JsonResourceCluster GENERAL_MSG;
    public static JsonResourceCluster ITEM_MSG;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        GENERAL_MSG = loadResourceDirectory("/lang/general");
        ITEM_MSG = loadResourceDirectory("/lang/items");

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

    private JsonResourceCluster loadResourceDirectory(String dirPath) {
        JsonResourceCluster cluster = new JsonResourceCluster();
        try {
            URI uri = Clara.class.getResource(dirPath).toURI();
            Path path;
            if(uri.getScheme().equals("jar")) {
                FileSystem system = FileSystems.getFileSystem(uri);
                path = system.getPath(dirPath);
            } else {
                path = Paths.get(uri);
            }
            cluster.traverseDirectory(path, (name, in) -> new JsonResourceLoader(in));



        } catch (Exception e) {
            e.printStackTrace();
        }
        cluster.makeDefault("en_US");
        return cluster;
    }
}
