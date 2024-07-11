package net.worldmc.merovingian.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.worldmc.merovingian.Merovingian;
import net.worldmc.merovingian.managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteListener implements Listener {
    private final DatabaseManager databaseManager;

    public VoteListener(Merovingian plugin) {
        this.databaseManager = plugin.getDatabaseManager();
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        Player player = Bukkit.getPlayer(vote.getUsername());

        if (player != null) {
            player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));

            int totalVotes = updateVoteCount(player);
            checkMilestones(player, totalVotes);
        }
    }

    private int updateVoteCount(Player player) {
        String sql = "INSERT INTO player_votes (uuid, vote_count) VALUES (?, 1) " +
                "ON DUPLICATE KEY UPDATE vote_count = vote_count + 1";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeUpdate();

            sql = "SELECT vote_count FROM player_votes WHERE uuid = ?";
            try (PreparedStatement preparedStatement2 = conn.prepareStatement(sql)) {
                preparedStatement2.setString(1, player.getUniqueId().toString());
                ResultSet rs = preparedStatement2.executeQuery();
                if (rs.next()) {
                    return rs.getInt("vote_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void checkMilestones(Player player, int totalVotes) {
        if (totalVotes == 100) {
            player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
        } else if (totalVotes == 200) {
            player.getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT, 8));
        } else if (totalVotes == 400) {
            player.getInventory().addItem(new ItemStack(Material.ELYTRA, 1));
        } else if (totalVotes == 800) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " minecraft:paper{display:{Name:'{\"text\":\"Owner's Secret\",\"italic\":false}'}}");
        }
    }
}