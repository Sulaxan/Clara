package me.encast.clara;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.encast.clara.boss.Sephrem;
import me.encast.clara.util.event.ArmorListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Clara extends JavaPlugin {

    @Getter
    private static Clara instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Sephrem.add();
//        Sephrem.registerEntity("Sephrem", 989, EntityPigZombie.class, Sephrem.class);
        getServer().getPluginManager().registerEvents(new ArmorListener(Lists.newArrayList()), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
