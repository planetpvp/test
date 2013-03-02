package fr.nc333.planetpvp;

import java.io.File;
import java.io.IOException;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import fr.nc333.planetpvp.Vote;

public class VoteConfig {
	
	
	Vote plugin;
	
	File fileConfig;
	FileConfiguration config;
	
	public VoteConfig(Vote plug, String file){
		
		plugin = plug;
		fileConfig = new File(plugin.getDataFolder() + file);
	}
	
	public void LoadConfig(){
		config = YamlConfiguration.loadConfiguration(fileConfig);
	}
	
	public void saveConfig() throws IOException {
		config.save(fileConfig);
	}
	
	public void addConfig(String key, Object value) {
		config.set(key, value);
	}
	
	public Object getConfigu(String path) {
		return config.get(path);
	}
	

}
