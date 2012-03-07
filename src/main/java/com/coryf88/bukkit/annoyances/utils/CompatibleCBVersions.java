package com.coryf88.bukkit.annoyances.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.craftbukkit.CraftServer;

public enum CompatibleCBVersions {
	UNKNOWN("Unknown", 0, 0),
	CB1_1_R5_SNAPSHOT("1.1-R5-SNAPSHOT", 1939, 1985),
	CB1_1_R5("1.1-R5", 1986),
	CB1_1_R6_SNAPSHOT("1.1-R6-SNAPSHOT", 1987),
	CB1_1_R6("1.1-R6", 1988),
	CB1_1_R7("1.1-R7", -1);

	//git-Bukkit-{VERSION}-b{BUILD}jnks
	// Note: Not all builds use this format, E.g. RB 1.1-R7
	private final static Pattern impVersionPattern = Pattern.compile("git-Bukkit-.*-b([0-9]+)jnks");

	private final String mavenVersion;
	private final int minBuild;
	private final int maxBuild;

	private CompatibleCBVersions(String mavenVersion, int build) {
		this.mavenVersion = mavenVersion;
		this.minBuild = build;
		this.maxBuild = build;
	}

	// Build numbers are inclusive
	private CompatibleCBVersions(String mavenVersion, int minBuild, int maxBuild) {
		this.mavenVersion = mavenVersion;
		this.minBuild = minBuild;
		this.maxBuild = maxBuild;
	}

	public static CompatibleCBVersions get() {
		// Get the version based on the maven version.
		InputStream is = CraftServer.class.getClassLoader().getResourceAsStream("META-INF/maven/org.bukkit/craftbukkit/pom.properties");
		if (is != null) {
			try {
				Properties properties = new Properties();
				properties.load(is);
				String mavenVersion = properties.getProperty("version");
				for (CompatibleCBVersions version : CompatibleCBVersions.values()) {
					if (version.mavenVersion.equals(mavenVersion)) return version;
				}
			} catch (IOException e) {}
		}

		// Get the version based on the build number from the implementation version.
		Matcher matcher = CompatibleCBVersions.impVersionPattern.matcher(CraftServer.class.getPackage().getImplementationVersion());
		if (matcher.matches()) {
			try {
				int build = Integer.parseInt(matcher.group(1));
				for (CompatibleCBVersions version : CompatibleCBVersions.values()) {
					if (version.minBuild < 0) {
						continue;
					}
					if (build >= version.minBuild && build <= version.maxBuild) return version;
				}
			} catch (NumberFormatException e) {}
		}

		// Give up
		return UNKNOWN;
	}
}
