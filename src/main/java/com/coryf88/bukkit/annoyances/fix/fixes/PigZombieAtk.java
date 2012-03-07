package com.coryf88.bukkit.annoyances.fix.fixes;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class PigZombieAtk extends Fix {
	private static HashMap<Integer, Entity> lastTargets = new HashMap<Integer, Entity>();

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() != EntityType.PIG_ZOMBIE) return;

		int pigZombieId = entity.getEntityId();

		TargetReason reason = event.getReason();
		Entity lastTarget = PigZombieAtk.lastTargets.get(pigZombieId);
		switch (reason) {
			case TARGET_ATTACKED_ENTITY: // Target attacked us
				PigZombieAtk.lastTargets.put(pigZombieId, entity);
				event.setTarget(entity);
				break;
			case CLOSEST_PLAYER:
				if (lastTarget != null) {
					event.setTarget(lastTarget);
				}
				break;
			case PIG_ZOMBIE_TARGET: // Target attacked a nearby pig zombie
				if (lastTarget != null) {
					event.setTarget(lastTarget);
				}
				break;
			case TARGET_DIED: // Target died
				PigZombieAtk.lastTargets.remove(pigZombieId);
				break;
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.PIG_ZOMBIE) {
			PigZombieAtk.lastTargets.remove(entity.getEntityId());
		}
	}

	public static String getConfigName() {
		return "PigZombieAtk";
	}
}
