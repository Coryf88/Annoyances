package com.coryf88.bukkit.annoyances.fix.fixes;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class DropBoats extends Fix {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		if (event.isCancelled()) return;

		Vehicle vehicle = event.getVehicle();
		if (vehicle.getType() == EntityType.BOAT) {
			vehicle.getWorld().dropItemNaturally(vehicle.getLocation(), new ItemStack(Material.BOAT, 1));
			if (!vehicle.isEmpty()) {
				vehicle.eject();
			}
			vehicle.remove();
			event.setCancelled(true);
		}
	}

	public static String getConfigName() {
		return "DropBoats";
	}
}