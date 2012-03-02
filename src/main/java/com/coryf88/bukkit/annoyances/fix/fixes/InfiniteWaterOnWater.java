package com.coryf88.bukkit.annoyances.fix.fixes;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFromToEvent;

import com.coryf88.bukkit.annoyances.fix.Fix;

public class InfiniteWaterOnWater extends Fix {
	private static final BlockFace[] sides = new BlockFace[] {BlockFace.NORTH, BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH};

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockFromTo(BlockFromToEvent event) {
		if (event.isCancelled()) return;
		Block from = event.getBlock();
		// Check if the block flowing from is a water source.
		if (this.isWaterSource(from)) {
			Block to = from.getRelative(event.getFace());
			if ((to.getType() == Material.AIR || this.isWater(to)) && !this.isWaterSource(to)) {
				Block toBelow = to.getRelative(BlockFace.DOWN);
				// Check if the block the water is spreading on-top of is also a water source.
				if (this.isWaterSource(toBelow)) {
					// Check if any side block above is also a water source.
					for (BlockFace side : InfiniteWaterOnWater.sides) {
						// Skip the from block
						if (side == event.getFace().getOppositeFace()) {
							continue;
						}
						Block sideBlock = to.getRelative(side);
						if (this.isWaterSource(sideBlock)) {
							to.setType(Material.WATER);
							to.setTypeIdAndData(8, (byte)0, true);
							break;
						}
					}
				}
			}
		}
	}

	private boolean isWater(Block block) {
		return block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER;
	}

	private boolean isWaterSource(Block block) {
		return this.isWater(block) && block.getData() == 0x0;
	}

	public static String getConfigName() {
		return "InfiniteWaterOnWater";
	}
}