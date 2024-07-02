package net.worldmc.merovingian.listeners;

import net.worldmc.merovingian.managers.DeathInventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final DeathInventoryManager deathInventoryManager;

    public PlayerDeathListener(DeathInventoryManager deathInventoryManager) {
        this.deathInventoryManager = deathInventoryManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        deathInventoryManager.removeCombatGear(player);
    }
}