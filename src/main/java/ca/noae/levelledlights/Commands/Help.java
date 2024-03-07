package ca.noae.levelledlights.Commands;

import ca.noae.levelledlights.LevelledLights;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

import static java.util.logging.Level.INFO;

public class Help implements CommandExecutor {
    private final LevelledLights plugin;

    public Help(LevelledLights plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getLogger().log(Level.INFO, sender.getName() + " ran help command");
        sender.sendMessage("no help for you :3");
        return true;
    }
}
