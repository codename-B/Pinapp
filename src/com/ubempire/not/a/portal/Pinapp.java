package com.ubempire.not.a.portal;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Pinapp extends JavaPlugin {
	public static ArrayList<Integer> portalTypes = new ArrayList<Integer>();
	private PinappPortal pinappPortal;
	private PinappPortalCreate pinappPC;
	public PinappConfig pc;

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		pc = new PinappConfig(this);
		pc.setDefaults();
		pc.setup();
		pinappPortal = new PinappPortal(this);
		pinappPortal.register();
		pinappPC = new PinappPortalCreate(this);
		pinappPC.register();
		log("Enabled.");
	}

	public void log(Object input) {
		System.out.println("[Pinapp] " + String.valueOf(input));
	}

	public static String locToString(Location loc) {
		String output;
		output = loc.getWorld().getName() + "," + loc.getBlockX() + ","
				+ loc.getBlockY() + "," + loc.getBlockZ();
		return output;
	}
}
