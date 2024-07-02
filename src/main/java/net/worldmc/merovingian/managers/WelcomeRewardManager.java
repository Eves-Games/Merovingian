package net.worldmc.merovingian.managers;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class WelcomeRewardManager {

    private final Plugin plugin;
    private UUID currentNewPlayer;
    private long welcomePeriodEndTime;
    private final Set<UUID> rewardedPlayers = new HashSet<>();

    private final Material rewardMaterial;
    private final int rewardAmount;
    private final long welcomePeriodDuration;
    private final Sound rewardSound;

    private ScheduledTask endWelcomePeriodTask;

    public WelcomeRewardManager(Plugin plugin) {
        this.plugin = plugin;

        FileConfiguration config = plugin.getConfig();
        String itemName = config.getString("welcome-reward.item", "GOLD_NUGGET");
        this.rewardMaterial = Material.valueOf(itemName);
        this.rewardAmount = config.getInt("welcome-reward.amount", 5);
        int timerSeconds = config.getInt("welcome-reward.timer", 10);
        this.welcomePeriodDuration = timerSeconds * 1000L; // Convert seconds to milliseconds
        String soundName = config.getString("welcome-reward.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
        this.rewardSound = Sound.valueOf(soundName);
    }

    public void startWelcomeRewardPeriod(Player newPlayer) {
        currentNewPlayer = newPlayer.getUniqueId();
        welcomePeriodEndTime = System.currentTimeMillis() + welcomePeriodDuration;
        rewardedPlayers.clear();

        // Cancel the previous task if it exists
        if (endWelcomePeriodTask != null) {
            endWelcomePeriodTask.cancel();
        }

        // Schedule the new task
        AsyncScheduler scheduler = Bukkit.getAsyncScheduler();
        endWelcomePeriodTask = scheduler.runDelayed(plugin, (task) -> {
            Bukkit.getServer().getRegionScheduler().execute(plugin, newPlayer.getLocation(), this::endWelcomePeriod);
        }, welcomePeriodDuration / 50, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public void handleWelcomeMessage(Player player) {
        if (currentNewPlayer == null || player.getUniqueId().equals(currentNewPlayer)) {
            return;
        }

        if (System.currentTimeMillis() <= welcomePeriodEndTime && !rewardedPlayers.contains(player.getUniqueId())) {
            giveReward(player);
            rewardedPlayers.add(player.getUniqueId());
        }
    }

    private void giveReward(Player player) {
        ItemStack rewardItem = new ItemStack(rewardMaterial, rewardAmount);
        player.getInventory().addItem(rewardItem);
        player.playSound(player.getLocation(), rewardSound, 1.0f, 1.0f);
    }

    private void endWelcomePeriod() {
        currentNewPlayer = null;
        rewardedPlayers.clear();
        endWelcomePeriodTask = null;
    }
}