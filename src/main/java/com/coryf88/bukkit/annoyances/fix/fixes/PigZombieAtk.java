package com.coryf88.bukkit.annoyances.fix.fixes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class PigZombieAtk extends Fix {
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.PIG_ZOMBIE && event.getReason() == TargetReason.TARGET_DIED) {
			((PigZombie)entity).setAngry(false);
		}
	}

	public static String getConfigName() {
		return "PigZombieAtk";
	}
}
