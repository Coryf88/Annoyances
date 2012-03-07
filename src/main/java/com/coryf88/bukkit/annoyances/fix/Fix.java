package com.coryf88.bukkit.annoyances.fix;

import org.bukkit.event.Listener;

import com.coryf88.bukkit.annoyances.Annoyances;

public abstract class Fix implements Listener {
	private String configName;

	/**
	 * Called when this fix is loaded
	 */
	public void onLoad() {}

	/**
	 * Called when this fix is enabled
	 */
	public void onEnable() {}

	/**
	 * Called when this fix is disabled
	 */
	public void onDisable() {}

	/**
	 * Called after the events have been registered.
	 */
	public void onEventsRegistered() {}

	/**
	 * Gets the requested Object by path.
	 * 
	 * If the Object does not exist but a default value has been specified, this will return the default value. If the Object does not exist and no default value was specified, this will return null.
	 * 
	 * @param name Name of the Object to get.
	 * @return Requested Object.
	 */
	protected Object getConfig(String name) {
		if (name != null) {
			String configName = this.getConfigName();
			if (configName != null) return Annoyances.getInstance().getConfig().get(configName + "." + name);
		}
		return null;
	}

	/**
	 * Gets the requested Object by path, returning a default value if not found.
	 * 
	 * If the Object does not exist then the specified default value will returned regardless of if a default has been identified in the root Configuration.
	 * 
	 * @param name Name of the Object to get.
	 * @param def The default value of the Object.
	 * @return Requested Object.
	 */
	protected Object getConfig(String name, Object def) {
		if (name != null) {
			String configName = this.getConfigName();
			if (configName != null) return Annoyances.getInstance().getConfig().get(configName + "." + name, def);
		}
		return null;
	}

	private String getConfigName() {
		if (this.configName == null) {
			try {
				this.configName = (String)this.getClass().getMethod("getConfigName", new Class<?>[0]).invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.configName;
	}
}
