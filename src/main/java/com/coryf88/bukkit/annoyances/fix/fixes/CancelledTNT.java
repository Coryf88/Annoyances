package com.coryf88.bukkit.annoyances.fix.fixes;

import java.util.HashSet;

import net.minecraft.server.ChunkPosition;
import net.minecraft.server.Packet60Explosion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class CancelledTNT extends Fix {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled()) {
			Location location = event.getLocation();
			((CraftServer)Bukkit.getServer()).getServer().serverConfigurationManager.sendPacketNearby(location.getX(), location.getY(), location.getZ(), 64, ((CraftWorld)location.getWorld()).getHandle().dimension, new Packet60Explosion(location.getX(), location.getY(), location.getZ(), 4.0F, new HashSet<ChunkPosition>()));
		}
	}

	public static String getConfigName() {
		return "CancelledTNT";
	}
}
