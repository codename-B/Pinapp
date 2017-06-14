package com.ubempire.not.a.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

public class PinappConfig {
	Pinapp jp;
	HashMap<Integer, String> ibw = new HashMap<Integer, String>();
	HashMap<String, Integer> wbi = new HashMap<String, Integer>();

	PinappConfig(Pinapp jp) {
		this.jp = jp;
	}

	public void setDefaults() {
        ConfigurationSection worldsSection = jp.getConfig().getConfigurationSection("worlds");
        if (worldsSection == null) {
			jp.log("No config found. Generating config file.");
			World defaultWorld = jp.getServer().getWorlds().get(0);
			String worldName = defaultWorld.getName();
            jp.getConfig().set("worlds." + worldName + ".generator", "Default");
            jp.getConfig().set("worlds." + worldName + ".id", 14);
            jp.getConfig().set("worlds." + worldName + ".env", "Normal");
            jp.getConfig().set("worlds." + worldName + ".seed", (int) defaultWorld.getSeed());
			jp.saveConfig();
		}
	}

	public void setup() {
		jp.log("Loading config.");
        ConfigurationSection ws = jp.getConfig().getConfigurationSection("worlds");
        if (ws == null) {
            this.setDefaults();
            ws = jp.getConfig().getConfigurationSection("worlds");
        }
		Set<String> keys = ws.getKeys(false);
		for (String key : keys) {
			// What is the gen?
			String generator = ws.getString(key + ".generator", "Default");
			// What is the portal block?
			int portalBlock = ws.getInt(key + ".id", 14);
			// What is the seed?
			long seed = (long) ws.getInt(key + ".seed", 0);
			// What is the env?
			String environment = ws.getString(key + ".env", "Normal");
			// Setup the env variables
			Environment env = Environment.NORMAL;
			if (environment.equalsIgnoreCase("nether"))
				env = Environment.NETHER;
			if (environment.equalsIgnoreCase("the_end"))
				env = Environment.THE_END;
			// Worldname?
			String worldName = key;
			// Is the gen other than default?
			if (!generator.equalsIgnoreCase("default")) {
				String[] genSplit = generator.split(":");
				String plugin = genSplit[0];
				String args = "";
				if (genSplit.length > 1)
					args = generator.substring(generator.indexOf(":") + 1);
				ChunkGenerator gen = jp.getServer().getPluginManager()
						.getPlugin(plugin)
						.getDefaultWorldGenerator(worldName, args);
				jp.log("Creating world: "+worldName);
				if(seed != 0)
				jp.getServer().createWorld(
                        new WorldCreator(worldName).environment(env).seed(seed).generator(gen));
				else
				jp.getServer().createWorld(
                        new WorldCreator(worldName).environment(env).generator(gen));
			} else {
				jp.log("Creating world: "+worldName);
				if(seed != 0)
				jp.getServer().createWorld(
                        new WorldCreator(worldName).environment(env).seed(seed));
				else
				jp.getServer().createWorld(
                        new WorldCreator(worldName).environment(env));
			}
			// Then do the stuff to let us manage it :D
			ibw.put(portalBlock, worldName);
			wbi.put(worldName, portalBlock);
			Pinapp.portalTypes.add(portalBlock);
		}
	}
	public Integer getId(String world) {
		return wbi.get(world);
	}
	public String getWorld(int id) {
		return ibw.get(id);
	}

	public String getWorld(Material mat) {
		return getWorld(mat.getId());
	}
}
