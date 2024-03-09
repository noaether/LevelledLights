package ca.noae.levelledlights.EventHandlers;

import ca.noae.levelledlights.LevelledLights;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class PlayerInteractEventHandler implements Listener {

    private final LevelledLights plugin;

    public PlayerInteractEventHandler(LevelledLights plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        //check if player is right clicking a block; if not, its not our problem
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player eventPlayer = event.getPlayer();
        Block eventBlock = event.getClickedBlock();

        String chunkCoordsX = String.valueOf(eventBlock.getX());
        String chunkCoordsZ = String.valueOf(eventBlock.getZ());

        Set<Material> transparentBlocks = null;

        List<Block> blocksInSight = eventPlayer.getLineOfSight(null, 10);
        for(Block block : blocksInSight) {
            eventPlayer.sendMessage("Block: " + block.getType().name());
        }

        event.setCancelled(false);
    }
}
