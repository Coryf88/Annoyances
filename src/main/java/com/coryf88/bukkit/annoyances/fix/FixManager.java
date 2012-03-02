package com.coryf88.bukkit.annoyances.fix;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import com.coryf88.bukkit.annoyances.Annoyances;
import com.coryf88.bukkit.annoyances.fix.fixes.CancelledTNT;
import com.coryf88.bukkit.annoyances.fix.fixes.DropBoats;
import com.coryf88.bukkit.annoyances.fix.fixes.DropSaddleOnDeath;
import com.coryf88.bukkit.annoyances.fix.fixes.EntityDrops;
import com.coryf88.bukkit.annoyances.fix.fixes.InfiniteWaterOnWater;
import com.coryf88.bukkit.annoyances.fix.fixes.KickMTQ;
import com.coryf88.bukkit.annoyances.fix.fixes.MeleeLegs;
import com.coryf88.bukkit.annoyances.fix.fixes.MobAtkDist;
import com.coryf88.bukkit.annoyances.fix.fixes.PigZombieAtk;
import com.coryf88.bukkit.annoyances.fix.fixes.PressurePlateSounds;
import com.coryf88.bukkit.annoyances.fix.fixes.SpawnProtection;
import com.coryf88.bukkit.annoyances.fix.fixes.VanillaSpamKicking;
import com.coryf88.bukkit.annoyances.fix.fixes.WheatBreed;

public class FixManager {
	private ArrayList<Class<? extends Fix>> fixes = new ArrayList<Class<? extends Fix>>();
	private List<Fix> loadedFixes = new ArrayList<Fix>();

	public FixManager() {
		this.fixes.add(CancelledTNT.class);
		this.fixes.add(DropBoats.class);
		this.fixes.add(DropSaddleOnDeath.class);
		this.fixes.add(EntityDrops.class);
		this.fixes.add(InfiniteWaterOnWater.class);
		this.fixes.add(KickMTQ.class);
		this.fixes.add(MeleeLegs.class);
		this.fixes.add(MobAtkDist.class);
		this.fixes.add(PigZombieAtk.class);
		this.fixes.add(PressurePlateSounds.class);
		this.fixes.add(SpawnProtection.class);
		this.fixes.add(VanillaSpamKicking.class);
		this.fixes.add(WheatBreed.class);
	}

	public void load() {
		FileConfiguration config = Annoyances.getInstance().getConfig();
		for (Class<? extends Fix> fixClass : this.fixes) {
			try {
				Method method = fixClass.getMethod("getConfigName", new Class<?>[0]);
				String configName = (String)method.invoke(null);
				// Load only if it's enabled in the config.
				if (configName != null && config.getBoolean(config.isBoolean(configName) ? configName : configName + ".Enable")) {
					Fix fix = fixClass.newInstance();
					this.loadedFixes.add(fix);
					fix.onLoad();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void enable() {
		// Enable the fixes
		for (Fix fix : this.loadedFixes) {
			fix.onEnable();
		}

		// Register fix events
		Annoyances instance = Annoyances.getInstance();

		PluginManager pm = instance.getServer().getPluginManager();
		for (Fix fix : this.loadedFixes) {
			pm.registerEvents(fix, instance);
		}

		// Execute onEventsRegistered after all events have been registered.
		for (Fix fix : this.loadedFixes) {
			fix.onEventsRegistered();
		}
	}

	public void disable() {
		// Disable the fixes
		for (Fix fix : this.loadedFixes) {
			fix.onDisable();

			// Unregister the events
			HandlerList.unregisterAll(fix);
		}
	}
}
