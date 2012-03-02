package com.coryf88.bukkit.annoyances.fix.fixes.mobatkdist;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.World;

import org.bukkit.entity.EntityType;

public class CustomZombie extends EntityZombie {
	public static float attackDistance = 2.0f;

	public CustomZombie(World world) {
		super(world);
	}

	@Override
	/**
	 * Attack entity
	 * 
	 * @param entity The entity to attack.
	 * @param f The distance between us and the entity we are attacking.
	 */
	protected void a(Entity entity, float f) {
		if (this.attackTicks <= 0 && f < CustomZombie.attackDistance && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) { // Annoyances
			this.attackTicks = 20;
			this.d(entity);
		}
	}

	/**
	 * @return The parent type of this custom mob.
	 */
	public static EntityType getParentType() {
		return EntityType.ZOMBIE;
	}
}