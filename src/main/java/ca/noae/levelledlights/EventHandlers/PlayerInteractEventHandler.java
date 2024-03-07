package ca.noae.levelledlights.EventHandlers;

import ca.noae.levelledlights.LevelledLights;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractEventHandler implements Listener {

    private final LevelledLights plugin;

    public PlayerInteractEventHandler(LevelledLights plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player eventPlayer = event.getPlayer();
        Block eventBlock = event.getClickedBlock();

        String chunkCoordsX = String.valueOf(eventBlock.getChunk().getX());
        String chunkCoordsZ = String.valueOf(eventBlock.getChunk().getZ());

        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
            plugin.getLogger().info(eventPlayer.getName() + " clicked on air at " + chunkCoordsX + "/" + chunkCoordsZ);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            plugin.getLogger().info(eventPlayer.getName() + " clicked on block at " + chunkCoordsX + "/" + chunkCoordsZ);
        }

        event.setCancelled(false);
    }
}
