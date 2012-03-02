package com.coryf88.bukkit.annoyances.fix.fixes;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.coryf88.bukkit.annoyances.fix.Fix;
import com.coryf88.bukkit.annoyances.fix.fixes.meleelegs.NetServerHandler;

public class MeleeLegs extends Fix {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		try {
			NetServerHandler.override(((CraftPlayer)player).getHandle().netServerHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEventsRegistered() {
		// Call onPlayerJoin for any player's currently online.
		for (Player player : Bukkit.getOnlinePlayers()) {
			this.onPlayerJoin(new PlayerJoinEvent(player, null));
		}
	}

	public static String getConfigName() {
		return "MeleeLegs";
	}
}