package com.ubempire.pinapp;

import java.util.List;

import org.bukkit.World.Environment;

public class WorldThread extends Thread {
	private Pinapp e;

	WorldThread(Pinapp e) {
		this.e = e;
	}

	public void run() {
		List<String> worldNames = e.worldNames;
		for (String world : worldNames) {
			String env = e.worldTypes.get(world);
			String portal = "null";
			if (e.worldMats.containsKey(world)) {
				portal = e.worldMats.get(world).name().toLowerCase();
			}
			if (env.equalsIgnoreCase("normal")) {
				System.out.println("Adding " + world + " with environment "
						+ env + ": portal type " + portal);
				e.getServer().createWorld(world, Environment.NORMAL);
			} else if (env.equalsIgnoreCase("nether")) {
				System.out.println("Adding " + world + " with environment "
						+ env + ": portal type " + portal);
				e.getServer().createWorld(world, Environment.NETHER);
			} else if (env.equalsIgnoreCase("skylands")) {
				System.out.println("Adding " + world + " with environment "
						+ env + ": portal type " + portal);
				e.getServer().createWorld(world, Environment.SKYLANDS);
			} else {
				System.err.println("Incorrectly formatted worldtype: " + env
						+ "for world " + world + "! World not added!");
			}
		}
		e.c = null;
		e.worldNames.clear();
		e.worldTypes.clear();
		e.worldNames.clear();
		e.worldMats.clear();
		e = null;
		this.interrupt();
	}
}
