package com.coryf88.bukkit.annoyances.fix.fixes.mobatkdist;

import net.minecraft.server.Entity;
import net.minecraft.server.EntitySpider;
import net.minecraft.server.MathHelper;
import net.minecraft.server.World;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent;

public class CustomSpider extends EntitySpider {
	public static float attackDistance = 2.0f;

	public CustomSpider(World world) {
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
		float f1 = this.a(1.0F);

		if (f1 > 0.5F && this.random.nextInt(100) == 0) {
			// CraftBukkit start
			EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null, EntityTargetEvent.TargetReason.FORGOT_TARGET);
			this.world.getServer().getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				if (event.getTarget() == null) {
					this.target = null;
				} else {
					this.target = ((CraftEntity)event.getTarget()).getHandle();
				}
				return;
			}
			// CraftBukkit end
		} else {
			if (f > 2.0F && f < 6.0F && this.random.nextInt(10) == 0) {
				if (this.onGround) {
					double d0 = entity.locX - this.locX;
					double d1 = entity.locZ - this.locZ;
					float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1);

					this.motX = d0 / f2 * 0.5D * 0.800000011920929D + this.motX * 0.20000000298023224D;
					this.motZ = d1 / f2 * 0.5D * 0.800000011920929D + this.motZ * 0.20000000298023224D;
					this.motY = 0.4000000059604645D;
				}
			} else {
				//super.a(entity, f);
				// Annoyances start
				if (this.attackTicks <= 0 && f < CustomSpider.attackDistance && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
					this.attackTicks = 20;
					this.d(entity);
				}
				// Annoyances end
			}
		}
	}

	/**
	 * @return The parent type of this custom mob.
	 */
	public static EntityType getParentType() {
		return EntityType.SPIDER;
	}
}
