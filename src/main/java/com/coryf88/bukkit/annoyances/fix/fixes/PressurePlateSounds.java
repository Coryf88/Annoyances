package com.coryf88.bukkit.annoyances.fix.fixes;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.material.PressurePlate;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class PressurePlateSounds extends Fix {
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		Block block = event.getBlock();
		if (block != null && (block.getType() == Material.STONE_PLATE || block.getType() == Material.WOOD_PLATE)) {
			Effect effect = ((PressurePlate)block.getState().getData()).isPressed() ? Effect.CLICK1 : Effect.CLICK2;
			block.getWorld().playEffect(block.getLocation(), effect, 0, 64);
		}
	}

	public static String getConfigName() {
		return "PressurePlateSounds";
	}
}
