package com.ubempire.pinapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class Pinapp extends JavaPlugin {
	public Configuration c;
	public List<String> worldNames = new ArrayList<String>();
	public HashMap<String, String> worldTypes = new HashMap<String, String>();
	public HashMap<String, Material> worldMats = new HashMap<String, Material>();
	public HashMap<Material, String> matWorlds = new HashMap<Material, String>();

	@Override
	public void onDisable() {
		System.out.println("[Pinappp] by codename_B disabled.");
	}

	@Override
	public void onEnable() {
		setupConfiguration();
		System.out.println("[Pinapp] by codename_B enabled.");
		System.out.println("[Pinapp] Remember, Pinapp is not a portal plugin!");
		Thread worldThread = new WorldThread(this);
		worldThread.run();
	}

	private void setupConfiguration() {
		c = this.getConfiguration();
		c.load();
		List<String> worlds = c.getKeys();
		for (String world : worlds) {
			worldNames.add(world);
			String env = c.getString(world + ".env", "normal");
			String portalString = c.getString(world + ".mat");
			int portalInt = c.getInt(world + ".mat", -1);
			Material mat = null;
			if (portalInt == -1)
				mat = Material.matchMaterial(portalString.toUpperCase());
			else
				mat = Material.getMaterial(portalInt);
			if (!worldTypes.containsKey(world))
				worldTypes.put(world, env);
			if (!matWorlds.containsKey(mat) && mat != null) {
				matWorlds.put(mat, world);
				worldMats.put(world, mat);
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("warp")
				|| cmd.getName().equalsIgnoreCase("w")
				|| cmd.getName().equalsIgnoreCase("pinapp")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Location loc = player.getLocation();
				Material m = Material.getMaterial(player.getWorld()
						.getBlockTypeIdAt(loc.add(0, -1, 0)));
				if (matWorlds.containsKey(m)) {
					World nWorld = getServer().getWorld(matWorlds.get(m));
					Location l = getSafeY(nWorld, loc);
					l.setY(getNextAir(nWorld, l));
					sender.sendMessage(ChatColor.GREEN + "[Pinapp] Warping... ");
					nWorld.loadChunk(nWorld.getChunkAt(l).getX(), nWorld
							.getChunkAt(l).getZ());
					player.teleport(l);
					nWorld.refreshChunk(nWorld.getChunkAt(l).getX(), nWorld
							.getChunkAt(l).getZ());
					return true;
				} else {
					sender.sendMessage(ChatColor.RED
							+ "[Pinapp] No world defined for blocktype "
							+ m.name().toLowerCase());
					return true;
				}
			} else {
				sender.sendMessage("Can't warp the console!");
				return true;
			}
		}
		return false;
	}

	private int getNextAir(World nWorld, Location l) {
		int x = l.getBlockX();
		int z = l.getBlockZ();
		for (int i = l.getBlockY(); (i < 128 && i < l.getBlockY() + 30); i++) {
			if (nWorld.getBlockTypeIdAt(x, i, z) == 0
					&& nWorld.getBlockTypeIdAt(x, i - 1, z) == 0)
				return i;
		}
		for (int i = l.getBlockY(); (i > 0 && i > l.getBlockY() - 30); i--) {
			if (nWorld.getBlockTypeIdAt(x, i, z) == 0
					&& nWorld.getBlockTypeIdAt(x, i + 1, z) == 0)
				return i + 1;
		}
		return 60;
	}

	public Location getSafeY(World w, Location l) {
		int x = l.getBlockX();
		int z = l.getBlockZ();
		int y = l.getBlockY();
		l = new Location(w, x, y, z);
		if (y < 5)
			y = 5;
		for (int i = w.getHighestBlockYAt(l); i >= y; i--) {
			if (w.getBlockTypeIdAt(x, i, z) == 0
					&& w.getBlockTypeIdAt(x, i - 1, z) == 0
					&& w.getBlockTypeIdAt(x, i - 2, z) != 0) {
				l.setY(i);
				return l;
			}
		}
		// BIGGER CHECK
		for (int X = x - 30; X <= x; X++) {
			for (int Z = z - 30; Z <= z; Z++) {
				if (w.getBlockTypeIdAt(X, y, Z) != 0) {
					l = new Location(w, X, y, Z);
					return l;
				}
			}
		}
		// BIGGER CHECK
		for (int X = x; X <= x + 30; X++) {
			for (int Z = z; Z <= z + 30; Z++) {
				if (w.getBlockTypeIdAt(X, y, Z) != 0) {
					l = new Location(w, X, y, Z);
					return l;
				}
			}
		}
		// BIGGER CHECKS
		for (int X = x; X <= x + 50; X++) {
			for (int Z = z; Z <= z + 50; Z++) {
				if (w.getBlockTypeIdAt(X, 64, Z) != 0) {
					l = new Location(w, X, 64, Z);
					return l;
				}
			}
		}
		// BIGGER CHECKS
		for (int X = x - 50; X <= x; X++) {
			for (int Z = z - 50; Z <= z; Z++) {
				if (w.getBlockTypeIdAt(X, 64, Z) != 0) {
					l = new Location(w, X, 64, Z);
					return l;
				}
			}
		}
		// BIGGER CHECKS
		for (int X = x; X <= x + 80; X++) {
			for (int Z = z; Z <= z + 80; Z++) {
				if (w.getBlockTypeIdAt(X, 40, Z) != 0) {
					l = new Location(w, X, 40, Z);
					return l;
				}
			}
		}
		// BIGGER CHECKS
		for (int X = x - 80; X <= x; X++) {
			for (int Z = z - 80; Z <= z; Z++) {
				if (w.getBlockTypeIdAt(X, 40, Z) != 0) {
					l = new Location(w, X, 40, Z);
					return l;
				}
			}
		}
		// BIGGER CHECKS
		for (int X = x - 80; X <= x + 80; X++) {
			for (int Z = z - 80; Z <= z + 80; Z++) {
				if (w.getBlockTypeIdAt(X, 60, Z) != 0) {
					l = new Location(w, X, 60, Z);
					return l;
				}
			}
		}
		return l;
	}
}
