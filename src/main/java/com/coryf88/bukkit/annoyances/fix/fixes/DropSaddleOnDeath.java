package com.coryf88.bukkit.annoyances.fix.fixes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class DropSaddleOnDeath extends Fix {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.PIG) {
			Pig pig = (Pig)entity;
			if (pig.hasSaddle()) {
				pig.getWorld().dropItemNaturally(pig.getLocation(), new ItemStack(Material.SADDLE, 1));
			}
		}
	}

	public static String getConfigName() {
		return "DropSaddleOnDeath";
	}
}
