package com.precipicegames.autoenchanter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Autoenchanter extends JavaPlugin {
    private YamlConfiguration config;
    public class ItemStatus extends HashMap<ItemStack, Double> {};
    private HashMap<Player, ItemStatus> trackedItems;
    
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
    	this.getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, new BlockListener(){
    		public void onBlockBreak(BlockBreakEvent event)
    		{
    			if(event.isCancelled())
    				return;
    			ItemStack item = event.getPlayer().getItemInHand();
    			System.out.println(this + " fired event!");
    			if(!config.isConfigurationSection(item.getType().toString()))
    			{
    				System.out.println(this + "Not found "+item.getType().toString());
    				return;
    			}
    			
				ConfigurationSection c = config.getConfigurationSection(item.getType().toString());
				System.out.println(this + " found block definition");
				if(!c.isConfigurationSection(BlockBreakEvent.class.getSimpleName()))
					return;
				
				ConfigurationSection subc = c.getConfigurationSection(BlockBreakEvent.class.getSimpleName());
				
				System.out.println(this + " found event listener!");
				
				int maxlevel = subc.getInt("MaxLevel", 1);
				Double rate = subc.getDouble("Rate",0.1);
				Double levelratefactor = subc.getDouble("LevelRateFactor",1);
				Double levelup = subc.getDouble("LevelupRequirement",10);
				
				String EnchantName = subc.getString("Enchant");
				if(EnchantName == null)
					return;
				System.out.println(this + " We have a working event!");
				Enchantment e = Enchantment.getByName(EnchantName);
				
				if(item.getEnchantmentLevel(e) >= maxlevel)
					return;
				
				if(!trackedItems.containsKey(event.getPlayer()))
					trackedItems.put(event.getPlayer(), new ItemStatus());
				
				if(!trackedItems.get(event.getPlayer()).containsKey(item))
					trackedItems.get(event.getPlayer()).put(item, new Double(0.0));
				
				Double trackedlevel = trackedItems.get(event.getPlayer()).get(item);
				Double newlevel = new Double(trackedlevel + rate/(levelratefactor));
				if(newlevel >= levelup)
				{
					trackedItems.get(event.getPlayer()).remove(item);
					item.addEnchantment(e, item.getEnchantmentLevel(e) + 1);
					trackedItems.get(event.getPlayer()).put(item, new Double(0.0));
					event.getPlayer().sendMessage(ChatColor.GREEN + "Congrats you have leveled up an Item!");
				}
				else
				{
					trackedItems.get(event.getPlayer()).put(item, newlevel);
				}
    		}
    	}, Priority.Monitor,this);
    	
    	
        System.out.println(this + " is now enabled!");
    }
}
