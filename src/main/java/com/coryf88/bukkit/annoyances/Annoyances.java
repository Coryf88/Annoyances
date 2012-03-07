package com.coryf88.bukkit.annoyances;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.coryf88.bukkit.annoyances.fix.FixManager;
import com.coryf88.bukkit.annoyances.utils.CompatibleCBVersions;

public class Annoyances extends JavaPlugin {
	private static Annoyances instance;
	private FixManager fixManager = new FixManager();

	@Override
	public void onEnable() {
		if (Annoyances.instance != null) {
			this.setEnabled(false);
			return;
		}

		// Check compatibility
		if (CompatibleCBVersions.get() == CompatibleCBVersions.UNKNOWN) {
			this.getLogger().severe("Not compatible with " + Bukkit.getVersion());
			this.setEnabled(false);
			return;
		}

		Annoyances.instance = this;

		this.getConfig().options().copyDefaults(true).copyHeader(true);
		this.saveConfig();

		this.fixManager.load();

		this.fixManager.enable();
	}

	@Override
	public void onDisable() {
		if (!Annoyances.instance.equals(this)) return;

		this.fixManager.disable();

		Annoyances.instance = null;
	}

	public static Annoyances getInstance() {
		return Annoyances.instance;
	}
}