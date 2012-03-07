package com.coryf88.bukkit.annoyances;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.coryf88.bukkit.annoyances.fix.FixManager;

public class Annoyances extends JavaPlugin {
	private static Annoyances instance;
	private FixManager fixManager = new FixManager();
	private Logger logger;
	public FileConfiguration config;

	@Override
	public void onEnable() {
		if (Annoyances.instance != null) {
			this.setEnabled(false);
			return;
		}

		this.logger = this.getLogger();

		// Check compatibility
		if (!Bukkit.getVersion().equals("git-Bukkit-1.1-R6-b1988jnks (MC: 1.1)")) {
			this.logger.severe("Only compatible with CraftBukkit version git-Bukkit-1.1-R6-b1988jnks (MC: 1.1)");
			this.setEnabled(false);
			return;
		}

		Annoyances.instance = this;

		this.config = this.getConfig();
		this.config.options().copyDefaults(true).copyHeader(true);
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