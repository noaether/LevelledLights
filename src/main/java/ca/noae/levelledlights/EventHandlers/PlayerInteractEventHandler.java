package ca.noae.levelledlights.EventHandlers;

import ca.noae.levelledlights.LevelledLights;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
    public void onEntityRightClick(PlayerInteractEntityEvent event) {
        if (
                event.getRightClicked().getType() == EntityType.SLIME
        ) {
            Block block = event.getRightClicked().getLocation().getBlock();
            block.setType(Material.LIGHT);

            final Levelled level = (Levelled) block.getBlockData();

            level.setLevel(level.getLevel() == 15 ? 0: level.getLevel() + 1);

            event.getPlayer().sendBlockChange(block.getLocation(), level);
        }
    }

    @EventHandler
    public void onEntityLeftClick(PrePlayerAttackEntityEvent event) {
        if (
                event.getAttacked().getType() == EntityType.SLIME
        ) {
            Block block = event.getAttacked().getLocation().getBlock();
            block.setType(Material.LIGHT);

            final Levelled level = (Levelled) block.getBlockData();

            level.setLevel(level.getLevel() == 0 ? 15: level.getLevel() - 1);

            event.getPlayer().sendBlockChange(block.getLocation(), level);
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player eventPlayer = event.getPlayer();
        Block lookingAt = eventPlayer.getTargetBlock(null, 10);
        if (lookingAt.getType() == Material.LIGHT) {
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
            if(isLookingAtLight.containsKey(eventPlayer.getUniqueId())) {
                isLookingAtLight.replace(eventPlayer.getUniqueId(), glowBlockID);
            } else {
                isLookingAtLight.put(eventPlayer.getUniqueId(), glowBlockID);
            }
        } else if (isLookingAtLight.containsKey(eventPlayer.getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    eventPlayer.getWorld().getEntity(isLookingAtLight.get(eventPlayer.getUniqueId())).remove();
                    isLookingAtLight.remove(eventPlayer.getUniqueId());
                }
            }.runTaskLater(LevelledLights.getInstance(), 20 * 2);
        }
    }
}
