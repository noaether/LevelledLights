package ca.noae.levelledlights;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LevelledLights extends JavaPlugin implements Listener {

    private static LevelledLights instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LevelledLights getInstance() {
        return instance;
    }
}
