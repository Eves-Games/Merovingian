package net.worldmc.merovingian;

import net.worldmc.merovingian.listeners.PlayerChatListener;
import net.worldmc.merovingian.listeners.PlayerDeathListener;
import net.worldmc.merovingian.listeners.PlayerJoinListener;
import net.worldmc.merovingian.managers.DeathInventoryManager;
import net.worldmc.merovingian.managers.WelcomeRewardManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Merovingian extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        WelcomeRewardManager welcomeRewardManager = new WelcomeRewardManager(this);
        DeathInventoryManager deathInventoryManager = new DeathInventoryManager();

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(deathInventoryManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(welcomeRewardManager, this), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(welcomeRewardManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
