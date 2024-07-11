package net.worldmc.merovingian.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.worldmc.merovingian.Merovingian;
import net.worldmc.merovingian.managers.WelcomeRewardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {
    private final WelcomeRewardManager welcomeRewardManager;

    public PlayerChatListener(Merovingian plugin) {
        this.welcomeRewardManager = new WelcomeRewardManager(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Component message = event.message();

        if (message.toString().toLowerCase().contains("welcome")) {
            welcomeRewardManager.handleWelcomeMessage(player);
        }
    }
}
