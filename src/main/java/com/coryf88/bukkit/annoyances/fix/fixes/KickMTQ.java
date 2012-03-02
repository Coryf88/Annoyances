package com.coryf88.bukkit.annoyances.fix.fixes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class KickMTQ extends Fix {
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.getReason().equals("You moved too quickly :( (Hacking?)")) {
			event.setCancelled(true);
		}
	}

	public static String getConfigName() {
		return "KickMTQ";
	}
}