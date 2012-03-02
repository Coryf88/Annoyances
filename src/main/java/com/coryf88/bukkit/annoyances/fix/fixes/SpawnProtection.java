package com.coryf88.bukkit.annoyances.fix.fixes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class SpawnProtection extends Fix {
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

		boolean canBreak = true;
		if (!player.isOp()) {
			int spawnRadius = Bukkit.getServer().getSpawnRadius();
			if (spawnRadius > 0) {
				Location locationDiff = block.getLocation().clone().subtract(block.getWorld().getSpawnLocation());
				int distanceFromSpawn = (int)Math.max(Math.abs(locationDiff.getX()), Math.abs(locationDiff.getZ()));
				canBreak = distanceFromSpawn > spawnRadius;
			}
		}

		if (!canBreak) {
			event.setCancelled(true);
		}
	}

	public static String getConfigName() {
		return "SpawnProtection";
	}
}