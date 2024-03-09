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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInteractEventHandler implements Listener {

    private static LevelledLights plugin;

    private Map<UUID /*player*/, UUID /*slime*/> isLookingAtLight = new HashMap<>();

    public PlayerInteractEventHandler(LevelledLights plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player eventPlayer = event.getPlayer();
        Block lookingAt = eventPlayer.getTargetBlock(null, 10);
        if (lookingAt.getType() == Material.LIGHT) {

            if(isLookingAtLight.containsKey(eventPlayer.getUniqueId())) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        UUID glowBlockID = isLookingAtLight.get(eventPlayer.getUniqueId());
                        if(glowBlockID == null) {
                            // looking at slime that is not in hashmap, but player just looked at a light...
                            // happens in case of a bug, or if there's a slime nearby that isn't a light
                            return;
                        }
                        Slime glowBlock = (Slime) eventPlayer.getWorld().getEntity(glowBlockID);
                        assert glowBlock != null; // we know there's a slime in the hashmap, so this should never be null (except if an admin/creative player kills the slime)
                        glowBlock.remove();
                        isLookingAtLight.remove(eventPlayer.getUniqueId()); // remove from hashmap
                        isLookingAtLight.remove(eventPlayer.getUniqueId()); // remove from hashmap
                    }
                }.runTaskLater(LevelledLights.getInstance(), 20 * 2);
                return;
            }

            Location blockLoc = new Location(eventPlayer.getWorld(), lookingAt.getX() + 0.5, lookingAt.getY() + 0.5, lookingAt.getZ() + 0.5);

            if(!eventPlayer.getWorld().getNearbyEntitiesByType(Slime.class, blockLoc, 0.5, 0.5, 0.5).isEmpty()) {
                return;
            }

            UUID glowBlockID = eventPlayer.getWorld().spawnEntity(blockLoc.add(0, -0.5, 0), EntityType.SLIME).getUniqueId();
            Slime glowBlock = (Slime) eventPlayer.getWorld().getEntity(glowBlockID);

            assert glowBlock != null;

            glowBlock.setSize(2);
            glowBlock.setGravity(false);
            glowBlock.setAI(false);
            glowBlock.setInvulnerable(true);
            glowBlock.setInvisible(true);
            glowBlock.setCollidable(false);
            glowBlock.setSilent(true);
            glowBlock.setGlowing(true);

            // Add player UUID to hashmap, along with the slime UUID
            isLookingAtLight.put(eventPlayer.getUniqueId(), glowBlockID);
        }
    }
}
