package me.encast.clara;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.encast.clara.command.ItemCommand;
import me.encast.clara.command.RtMemoryFootprintCommand;
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
import java.util.Collections;

@Getter
public final class Clara extends JavaPlugin {

    @Getter
    private static Clara instance;

    private ItemManager itemManager;
    private ClaraPlayerManager playerManager;
    private InventoryManager inventoryManager;

    public static JsonResourceCluster GENERAL_MSG;
    public static JsonResourceCluster ITEM_MSG;

    public static Gson GSON = new Gson();
    public static Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

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

        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        // Managers
        this.itemManager = new ItemManager(this);
        this.playerManager = new ClaraPlayerManager(this);
        this.inventoryManager = new InventoryManager(this);

        getCommand("item").setExecutor(new ItemCommand());
        // "Realtime Memory Footprint"
        getCommand("rtmf").setExecutor(new RtMemoryFootprintCommand());
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
            FileSystem system = null;
            if(uri.getScheme().equals("jar")) {
                // Try to create a new file system, if it exists, get the already existing one
                try {
                    system = FileSystems.newFileSystem(uri, Collections.emptyMap());
                } catch (Exception e) {
                    system = FileSystems.getFileSystem(uri);
                }
                path = system.getPath(dirPath);
            } else {
                path = Paths.get(uri);
            }
            cluster.traverseDirectory(path, (name, in) -> new JsonResourceLoader(in));

            // Close the file system, we don't need it anymore for now
            if(system != null)
                system.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cluster.makeDefault("en_US");
        return cluster;
    }
}
