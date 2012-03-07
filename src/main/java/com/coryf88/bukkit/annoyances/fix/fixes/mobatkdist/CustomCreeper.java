package com.coryf88.bukkit.annoyances.fix.fixes.mobatkdist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.World;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class CustomCreeper extends EntityCreeper {
	public static float attackDistance = 3.0f;
	public static float defuseDistance = 7.0f;

	public CustomCreeper(World world) {
		super(world);
	}

	/**
	 * Attack entity
	 * 
	 * @param entity The entity to attack.
	 * @param f The distance between us and the entity we are attacking.
	 */
	@Override
	protected void a(Entity entity, float f) {
		if (!this.world.isStatic) {
			try {
				int fuseTicks = this.getFuseTicks();

				int i = this.B();

				if ((i > 0 || f >= CustomCreeper.attackDistance) && (i <= 0 || f >= CustomCreeper.defuseDistance)) { // Annoyances
					this.b(-1);
					--fuseTicks;
					if (fuseTicks < 0) {
						fuseTicks = 0;
					}
				} else {
					if (fuseTicks == 0) {
						this.world.makeSound(this, "random.fuse", 1.0F, 0.5F);
					}

					this.b(1);
					++fuseTicks;
					if (fuseTicks >= 30) {
						// CraftBukkit start
						float radius = this.isPowered() ? 6.0F : 3.0F;

						ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), radius, false);
						this.world.getServer().getPluginManager().callEvent(event);

						if (!event.isCancelled()) {
							this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire());
							this.die();
						} else {
							fuseTicks = 0;
						}
						// CraftBukkit end
					}

					this.e = true;
				}

				this.setFuseTicks(fuseTicks);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// return super.fuseTicks;
	private int getFuseTicks() {
		try {
			Field field = this.getClass().getSuperclass().getDeclaredField("fuseTicks");
			field.setAccessible(true);
			return ((Integer)field.get(this)).intValue();
		} catch (Exception e) {
			throw new RuntimeException("Failed to get field 'fuseTicks' from " + this.getClass(), e);
		}
	}

	// super.fuseTicks = fuseTicks;
	private void setFuseTicks(int fuseTicks) {
		try {
			Field field = this.getClass().getSuperclass().getDeclaredField("fuseTicks");
			field.setAccessible(true);
			field.set(this, fuseTicks);
		} catch (Exception e) {
			throw new RuntimeException("Failed to set field 'fuseTicks' in " + this.getClass(), e);
		}
	}

	// return super.B();
	private int B() {
		//return this.datawatcher.getByte(16);
		try {
			Method method = this.getClass().getSuperclass().getDeclaredMethod("B");
			method.setAccessible(true);
			return ((Integer)method.invoke(this)).intValue();
		} catch (Exception e) {
			throw new RuntimeException("Failed to call method 'B' from " + this.getClass(), e);
		}
	}

	// super.b(i);
	private void b(int i) {
		//this.datawatcher.watch(16, Byte.valueOf((byte) i));
		try {
			Method method = this.getClass().getSuperclass().getDeclaredMethod("b", int.class);
			method.setAccessible(true);
			method.invoke(this, i);
		} catch (Exception e) {
			throw new RuntimeException("Failed to call method 'b' from " + this.getClass(), e);
		}
	}

	/**
	 * @return The parent type of this custom mob.
	 */
	public static EntityType getParentType() {
		return EntityType.CREEPER;
	}
}
