package net.worldmc.merovingian.managers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DeathInventoryManager {

    private static final Set<Material> COMBAT_MATERIALS = new HashSet<>(Arrays.asList(
            // Weapons
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
            Material.BOW, Material.CROSSBOW, Material.TRIDENT,
            // Armor
            Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET,
            Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS,
            Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS,
            Material.SHIELD
    ));

    private static final Set<Material> GOLD_MATERIALS = new HashSet<>(Arrays.asList(
            Material.GOLD_INGOT,
            Material.GOLD_BLOCK,
            Material.GOLD_NUGGET
    ));

    private final Random random = new Random();

    public void removeCombatGear(Player player) {
        PlayerInventory inventory = player.getInventory();
        World world = player.getWorld();
        Location dropLocation = player.getLocation();

        // Remove and drop combat items from main inventory
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && COMBAT_MATERIALS.contains(item.getType())) {
                inventory.setItem(i, null);
                world.dropItemNaturally(dropLocation, item);
            }
        }

        // Remove and drop armor
        for (ItemStack armorPiece : inventory.getArmorContents()) {
            if (armorPiece != null && COMBAT_MATERIALS.contains(armorPiece.getType())) {
                inventory.remove(armorPiece);
                world.dropItemNaturally(dropLocation, armorPiece);
            }
        }

        // Remove and drop offhand item if it's combat gear
        ItemStack offhandItem = inventory.getItemInOffHand();
        if (COMBAT_MATERIALS.contains(offhandItem.getType())) {
            inventory.setItemInOffHand(null);
            world.dropItemNaturally(dropLocation, offhandItem);
        }

        dropHalfGold(player);

        player.updateInventory();
    }

    private void dropHalfGold(Player player) {
        PlayerInventory inventory = player.getInventory();
        World world = player.getWorld();
        Location dropLocation = player.getLocation();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && GOLD_MATERIALS.contains(item.getType())) {
                int amount = item.getAmount();
                int amountToDrop = amount / 2;
                if (amountToDrop == 0 && random.nextBoolean()) {
                    amountToDrop = 1;
                }
                if (amountToDrop > 0) {
                    ItemStack dropStack = new ItemStack(item.getType(), amountToDrop);
                    world.dropItemNaturally(dropLocation, dropStack);
                    item.setAmount(amount - amountToDrop);
                }
            }
        }
    }
}