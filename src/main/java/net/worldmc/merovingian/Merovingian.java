package net.worldmc.merovingian;

import net.worldmc.merovingian.listeners.PlayerChatListener;
import net.worldmc.merovingian.listeners.PlayerDeathListener;
import net.worldmc.merovingian.listeners.PlayerJoinListener;
import net.worldmc.merovingian.listeners.VoteListener;
import net.worldmc.merovingian.managers.DatabaseManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Merovingian extends JavaPlugin {
    private static Merovingian instance;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        databaseManager = new DatabaseManager(this);
        databaseManager.connect();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerChatListener(this), this);
        pluginManager.registerEvents(new VoteListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Merovingian getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
