package ca.noae.levelledlights;

import ca.noae.levelledlights.Commands.Help;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import ca.noae.levelledlights.EventHandlers.PlayerInteractEventHandler;

public final class LevelledLights extends JavaPlugin {

    private static LevelledLights instance;

    private FileConfiguration database;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        createDatabase();

        this.getCommand("help").setExecutor(new ca.noae.levelledlights.Commands.Help(this));
        new ca.noae.levelledlights.EventHandlers.PlayerInteractEventHandler(this);

        new ca.noae.levelledlights.GenericHandlers.ConfigHandler(this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }

    public FileConfiguration getDatabase() {
        return this.database;
    }

    private void createDatabase() {
        File databaseFile = new File(getDataFolder(), "database.yml");
        if(!databaseFile.exists()) {
            databaseFile.getParentFile().mkdirs();
            saveResource("database.yml", false);
        }

        database = new YamlConfiguration();

        try{
            database.load(databaseFile);
        } catch (IOException | InvalidConfigurationException e) {
            instance.getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    public static LevelledLights getInstance() {
        return instance;
    }
}
