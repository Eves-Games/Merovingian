package net.worldmc.merovingian.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.worldmc.merovingian.managers.WelcomeRewardManager;
import net.worldmc.merovingian.Merovingian;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final WelcomeRewardManager welcomeRewardManager;

    public PlayerJoinListener(Merovingian plugin) {
        this.welcomeRewardManager = new WelcomeRewardManager(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);

        Player player = event.getPlayer();
        TagResolver playerNamePlaceholder = Placeholder.component("player", Component.text(player.getName()));

        if (!player.hasPlayedBefore()) {
            Component newJoinMessage = MiniMessage.miniMessage().deserialize(
                    Merovingian.getInstance().getConfig().getString("messages.new-join-message", "<gray>[<b><gradient:#00AA00:#FFAA00>WorldMC</gradient></b>] <green>Welcome to the server for the first time, <player>!"),
                    playerNamePlaceholder
            );
            Bukkit.getServer().sendMessage(newJoinMessage);

            welcomeRewardManager.startWelcomeRewardPeriod(player);
        } else {
            Component joinMessage = MiniMessage.miniMessage().deserialize(
                    Merovingian.getInstance().getConfig().getString("messages.join-message", "<gray>[<b><gradient:#00AA00:#FFAA00>WorldMC</gradient></b>] <green>Welcome back, <player>!"),
                    playerNamePlaceholder
            );
            Bukkit.getServer().sendMessage(joinMessage);
        }
    }
}