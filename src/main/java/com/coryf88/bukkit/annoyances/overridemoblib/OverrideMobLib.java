package com.coryf88.bukkit.annoyances.overridemoblib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.BiomeMeta;
import net.minecraft.server.EnumCreatureType;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Slime;
import org.bukkit.entity.WaterMob;

/**
 * This was made specifically for this plugin, it may or may not be released as a library in the future.
 */
public class OverrideMobLib {
	public static void override(Class<?> clazz) throws OverrideMobException {
		EntityType parentType = OverrideMobLib.getClassParentType(clazz);
		OverrideMobLib.overrideMobSpawner(clazz, parentType);
		OverrideMobLib.overrideBiomeSpawn(clazz, parentType);

		// Respawn all current mobs
		for (World world : Bukkit.getServer().getWorlds()) {
			for (Entity entity : world.getEntities()) {
				EntityType entityType = entity.getType();
				if (entityType != null) {
					OverrideMobLib.respawnEntity(entity, entityType);
				}
			}
		}
	}

	private static void respawnEntity(Entity entity, EntityType entityType) {
		if (!(entity instanceof LivingEntity)) return;
		OverrideMobLib.respawnEntity((LivingEntity)entity, entityType);
	}

	private static void respawnEntity(LivingEntity livingEntity, EntityType entityType) {
		if (livingEntity.isDead()) return;

		LivingEntity newLivingEntity = livingEntity.getWorld().spawnCreature(livingEntity.getLocation(), entityType);

		switch (entityType) {
			case SLIME:
			case MAGMA_CUBE:
				((Slime)newLivingEntity).setSize(((Slime)livingEntity).getSize());
				break;
		}

		newLivingEntity.setFallDistance(livingEntity.getFallDistance());
		newLivingEntity.setFireTicks(livingEntity.getFireTicks());
		newLivingEntity.setHealth(livingEntity.getHealth());
		newLivingEntity.setLastDamage(livingEntity.getLastDamage());
		newLivingEntity.setLastDamageCause(livingEntity.getLastDamageCause());
		newLivingEntity.setMaximumAir(livingEntity.getMaximumAir());
		newLivingEntity.setMaximumNoDamageTicks(livingEntity.getMaximumNoDamageTicks());
		newLivingEntity.setNoDamageTicks(livingEntity.getNoDamageTicks());
		newLivingEntity.setPassenger(livingEntity.getPassenger());
		newLivingEntity.setRemainingAir(livingEntity.getRemainingAir());

		// IllegalArgumentException("Age must be at least 1 tick")
		newLivingEntity.setTicksLived(livingEntity.getTicksLived() > 0 ? livingEntity.getTicksLived() : 1);

		newLivingEntity.setVelocity(livingEntity.getVelocity());

		if (livingEntity.isInsideVehicle()) {
			Entity entity = livingEntity.getVehicle();
			if (entity != null) {
				entity.eject();
				entity.setPassenger(newLivingEntity);
			}
		}

		livingEntity.remove();
	}

	/**
	 * Overrides naturally spawned mobs.
	 * 
	 * @param clazz The class that will replace the original mob.
	 * @param parentType The replacement's parent mob information.
	 * @throws CustomMobException If an exception occurs while overriding.
	 */
	private static void overrideBiomeSpawn(Class<?> clazz, EntityType parentType) throws OverrideMobException {
		try {
			Field a = net.minecraft.server.BiomeBase.class.getField("biomes");
			BiomeBase[] biomes = (BiomeBase[])a.get(null);
			for (BiomeBase biome : biomes) { // Check all biomes.
				if (biome == null) {
					continue;
				}
				@SuppressWarnings("unchecked")
				ArrayList<BiomeMeta> creatures = (ArrayList<BiomeMeta>)biome.getMobs(OverrideMobLib.getCreatureType(parentType.getEntityClass()));
				for (BiomeMeta creature : creatures) { // Loop through all creatures.
					if (creature.a.equals(parentType.getEntityClass())) { // Check if we should override it.
						//System.out.print("Replacing " + creature.a.getSimpleName() + " with " + clazz.getSimpleName() + " in " + biome.getClass().getSimpleName());
						creature.a = clazz;
						break;
					}
				}
			}
		} catch (Exception e) {
			throw new OverrideMobException("Failed to override natural spawning with custom mob " + clazz.getSimpleName(), e);
		}
	}

	/**
	 * Overrides spawned mobs from mob spawners.
	 * 
	 * @param clazz The class that will replace the original mob.
	 * @param parentType The replacement's parent mob information.
	 * @throws CustomMobException If an exception occurs while overriding.
	 */
	private static void overrideMobSpawner(Class<?> clazz, EntityType parentType) throws OverrideMobException {
		try {
			Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
			a.setAccessible(true);
			a.invoke(a, clazz, parentType.getName(), parentType.getTypeId());
		} catch (Exception e) {
			throw new OverrideMobException("Failed to override mob spawner with custom mob " + clazz.getSimpleName(), e);
		}
	}

	/**
	 * Get's the replacement's parent mob information.
	 * 
	 * @param clazz The class to get the parent mob information from.
	 * @return The parent mob information.
	 * @throws CustomMobException If the class doesn't define the parent mob information.
	 */
	private static EntityType getClassParentType(Class<?> clazz) throws OverrideMobException {
		try {
			Method getParentType = clazz.getMethod("getParentType");
			return (EntityType)getParentType.invoke(null);
		} catch (Exception e) {
			throw new OverrideMobException(clazz.getSimpleName() + " is an invalid custom mob, mob does not declare getParentType static method", e);
		}
	}

	private static EnumCreatureType getCreatureType(Class<? extends Entity> clazz) {
		if (Animals.class.isAssignableFrom(clazz))
			return EnumCreatureType.CREATURE;
		else if (Monster.class.isAssignableFrom(clazz))
			return EnumCreatureType.MONSTER;
		else if (WaterMob.class.isAssignableFrom(clazz))
			return EnumCreatureType.WATER_CREATURE;
		return null;
	}
}