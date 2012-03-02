package com.coryf88.bukkit.annoyances.fix.fixes;

import com.coryf88.bukkit.annoyances.fix.Fix;
import com.coryf88.bukkit.annoyances.fix.fixes.mobatkdist.CustomCreeper;
import com.coryf88.bukkit.annoyances.fix.fixes.mobatkdist.CustomSpider;
import com.coryf88.bukkit.annoyances.fix.fixes.mobatkdist.CustomZombie;
import com.coryf88.bukkit.annoyances.overridemoblib.OverrideMobException;
import com.coryf88.bukkit.annoyances.overridemoblib.OverrideMobLib;

public class MobAtkDist extends Fix {
	@Override
	public void onEnable() {
		CustomCreeper.attackDistance = ((Double)this.getConfig("CreeperIgnite", 3.0)).floatValue();
		CustomCreeper.defuseDistance = ((Double)this.getConfig("CreeperDefuse", 7.0)).floatValue();

		CustomZombie.attackDistance = ((Double)this.getConfig("Zombie", 2.0)).floatValue();

		CustomSpider.attackDistance = ((Double)this.getConfig("Spider", 2.0)).floatValue();

		try {
			OverrideMobLib.override(CustomCreeper.class);
			OverrideMobLib.override(CustomSpider.class);
			OverrideMobLib.override(CustomZombie.class);
		} catch (OverrideMobException e) {
			throw new RuntimeException("Failed to override mobs", e);
		}
	}

	public static String getConfigName() {
		return "MobAtkDist";
	}
}