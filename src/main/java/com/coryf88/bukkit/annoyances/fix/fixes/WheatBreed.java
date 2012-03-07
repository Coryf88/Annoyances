package com.coryf88.bukkit.annoyances.fix.fixes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.server.EntityAnimal;

import org.bukkit.craftbukkit.entity.CraftAnimals;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class WheatBreed extends Fix {
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity target = event.getRightClicked();
		if (player != null && target != null && Animals.class.isAssignableFrom(target.getClass())) {
			ItemStack holding = player.getItemInHand();
			if (holding != null) {
				CraftAnimals craftAnimal = (CraftAnimals)target;
				if (this.isBreedingItem(holding, craftAnimal) && !this.canBreed(craftAnimal)) {
					event.setCancelled(true);
				}
			}
		}
	}

	// 1818, 1840, 1846, 1938, 1986, 1988: private boolean a(nms.ItemStack)
	private boolean isBreedingItem(ItemStack itemstack, CraftAnimals craftAnimal) {
		try {
			Method method = EntityAnimal.class.getDeclaredMethod("a", net.minecraft.server.ItemStack.class);
			method.setAccessible(true);
			return (Boolean)method.invoke(craftAnimal.getHandle(), ((CraftItemStack)itemstack).getHandle());
		} catch (Exception e) {
			throw new RuntimeException("Exception occured in WheatBreed fix", e);
		}
	}

	private boolean canBreed(CraftAnimals craftAnimal) {
		// Don't need to check age here, since age is checked internally, but we check it anyway.
		return !this.inLoveMode(craftAnimal) && craftAnimal.getAge() == 0;
	}

	// 1818, 1840, 1846, 1938, 1986, 1988: private int love;
	private boolean inLoveMode(CraftAnimals craftAnimal) {
		try {
			Field field = EntityAnimal.class.getDeclaredField("love");
			field.setAccessible(true);
			return field.getInt(craftAnimal.getHandle()) > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getConfigName() {
		return "WheatBreed";
	}
}
