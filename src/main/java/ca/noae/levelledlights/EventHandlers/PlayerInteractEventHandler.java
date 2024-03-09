package ca.noae.levelledlights.EventHandlers;

import ca.noae.levelledlights.LevelledLights;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Collection;
import java.util.List;

public class PlayerInteractEventHandler implements Listener {

    public PlayerInteractEventHandler(LevelledLights plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        //check if player is right-clicking a block; if not, it's not our problem
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player eventPlayer = event.getPlayer();

        List<Block> blocksInSight = eventPlayer.getLineOfSight(null, 100);

        for(Block block : blocksInSight) {
            if(block.getType() == Material.LIGHT) {
                eventPlayer.sendMessage("You clicked a light!");
                eventPlayer.getWorld().spawnParticle(org.bukkit.Particle.FLAME, block.getLocation(), 10);
                return;
            }
        }

        event.setCancelled(false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player eventPlayer = event.getPlayer();
        Block lookingAt = eventPlayer.getTargetBlock(null, 10);
        if(lookingAt.getType() == Material.LIGHT) {
            Location blockLoc = new Location(eventPlayer.getWorld(), lookingAt.getX() + 0.5, lookingAt.getY() + 0.5, lookingAt.getZ() + 0.5);
            Collection<FallingBlock> pre_shulkerCollection = eventPlayer.getWorld().getNearbyEntitiesByType(FallingBlock.class, blockLoc.add(0, 0, 0), 5);

            if(pre_shulkerCollection.isEmpty()){
                eventPlayer.getWorld().spawnEntity(blockLoc.add(0, 1, 0), EntityType.FALLING_BLOCK);

                eventPlayer.getWorld().getEntitiesByClass(FallingBlock.class).forEach(glowBlock -> {
                    glowBlock.setGravity(false);
                    glowBlock.teleport(glowBlock.getLocation().add(0, 0, 0));
                    glowBlock.setGlowing(true);
                });
            }

            eventPlayer.getWorld().spawnParticle(org.bukkit.Particle.FLAME, blockLoc, 100);
        }
    }
}
