package ca.noae.levelledlights.EventHandlers;

import ca.noae.levelledlights.LevelledLights;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;

public class PlayerInteractEventHandler implements Listener {

    private static LevelledLights plugin;

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
            Collection<Slime> pre_shulkerCollection = eventPlayer.getWorld().getNearbyEntitiesByType(Slime.class, blockLoc.add(0, 0, 0), 5);

            if(pre_shulkerCollection.isEmpty()){
                eventPlayer.getWorld().spawnEntity(blockLoc, EntityType.SLIME);

                eventPlayer.getWorld().getEntitiesByClass(Slime.class).forEach(glowBlock -> {
                    glowBlock.setSize(1);
                    glowBlock.setGravity(false);
                    glowBlock.setAI(false);
                    glowBlock.setInvulnerable(true);
                    glowBlock.setInvisible(true);
                    glowBlock.setCollidable(false);
                    glowBlock.setSilent(true);
                    glowBlock.setGlowing(true);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            glowBlock.remove();
                        }
                    }.runTaskLater(LevelledLights.getInstance(), 20 * 5);
                });
            }
        }
    }
}
