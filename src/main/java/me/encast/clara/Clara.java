package me.encast.clara;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.encast.clara.command.ItemCommand;
import me.encast.clara.item.ItemManager;
import me.encast.clara.player.ClaraPlayerManager;
import me.encast.clara.util.event.ArmorListener;
import me.encast.clara.util.inventory.invx.InventoryManager;
import me.encast.clara.util.resource.JsonResourceLoader;
import me.encast.clara.util.resource.ResourceCluster;
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

    public static ResourceCluster MSG_RESOURCE;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        MSG_RESOURCE = new ResourceCluster();
        try {
            URI uri = Clara.class.getResource("/lang/messages").toURI();
            Path path;
            if(uri.getScheme().equals("jar")) {
                FileSystem system = FileSystems.getFileSystem(uri);
                path = system.getPath("/lang/messages");
            } else {
                path = Paths.get(uri);
            }
            MSG_RESOURCE.traverseDirectory(path, (name, in) -> new JsonResourceLoader(in));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
