package com.coryf88.bukkit.annoyances.fix.fixes;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class EntityDrops extends Fix {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		final Entity entity = event.getEntity();
		final World world = entity.getWorld();
		final Location location = entity.getLocation();
		List<ItemStack> drops = event.getDrops();
		for (ItemStack stack : drops) {
			world.dropItem(location, stack);
		}
		drops.clear();
	}

	public static String getConfigName() {
		return "EntityDrops";
	}
}
