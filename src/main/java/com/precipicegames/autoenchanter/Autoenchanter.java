package com.precipicegames.autoenchanter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.Listener;

import org.bukkit.entity.Player;

import com.precipicegames.autoenchanter.listeners.BlockLstn;
import com.precipicegames.autoenchanter.listeners.PlayerLstn;

public class Autoenchanter extends JavaPlugin{
    public YamlConfiguration config;
    public HashMap<Player, ItemStatus> trackedItems;
    
	public void onDisable() {
        // TODO: Place any custom disable code here.
        System.out.println(this + " is now disabled!");
    }

    public void onEnable() {
    	this.getDataFolder().mkdirs();
    	File f = new File(this.getDataFolder(),"config.yml");
    	try {
			f.createNewFile();
		} catch (IOException e1) {
    		System.out.println(this + ": Error creating configuration file");
    		return;
		}
    	

    	config = new YamlConfiguration();
    	try {
			config.load(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		trackedItems = new HashMap<Player, ItemStatus>();
		
		
		//Register Block break event
		Listener block = new BlockLstn(this);
		Listener plListen = new PlayerLstn(this);
    	this.getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, block , Priority.Monitor,this);
    	this.getServer().getPluginManager().registerEvent(Type.BLOCK_PLACE, block , Priority.Monitor,this);
    	this.getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, plListen , Priority.Monitor,this);
    	this.getServer().getPluginManager().registerEvent(Type.PLAYER_PICKUP_ITEM, plListen , Priority.Monitor,this);
    	this.getServer().getPluginManager().registerEvent(Type.PLAYER_FISH, plListen , Priority.Monitor,this);
    	
    	
        System.out.println(this + " is now enabled!");
    }

}
