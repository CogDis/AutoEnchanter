package com.precipicegames.autoenchanter.listeners;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import com.precipicegames.autoenchanter.Autoenchanter;
import com.precipicegames.autoenchanter.ItemStatus;
import com.precipicegames.autoenchanter.UniqueItem;

public class PlayerLstn extends PlayerListener {
	private Autoenchanter plugin;
	public PlayerLstn(Autoenchanter p)
	{
		plugin = p;
	}
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		ConfigurationSection c = this.playerEventConfig("PlayerInteractEvent", event);
		ConfigurationSection extended = c.getConfigurationSection(event.getAction().toString());
		if(extended != null)	
		{
			Configuration conf = new MemoryConfiguration();
			for(String setting : c.getKeys(true))
			{
				conf.set(setting, c.get(setting));
			}
			for(String setting : extended.getKeys(true))
			{
				conf.set(setting, extended.get(setting));
			}
			this.playerAction(conf, event.getPlayer());
		}
		else
		{
			this.playerAction(c, event.getPlayer());
		}
	}
	public void onPlayerPortal(PlayerPortalEvent event)
	{
		ConfigurationSection c = this.playerEventConfig("PlayerPortalEvent", event);
		this.playerAction(c, event.getPlayer());
	}
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		ConfigurationSection c = this.playerEventConfig("PlayerPickupItemEvent", event);
		this.playerAction(c, event.getPlayer());
	}
	public void onPlayerFish(PlayerFishEvent event)
	{
		ConfigurationSection c = this.playerEventConfig("PlayerfishEvent", event);
		this.playerAction(c, event.getPlayer());
	}
	public ConfigurationSection playerEventConfig(String eventName, PlayerEvent event)
	{
		Player p = event.getPlayer();
		UniqueItem item = new UniqueItem(p.getItemInHand());
		if(!plugin.config.isConfigurationSection(item.get().getType().toString()))
			return null;
		
		ConfigurationSection c = plugin.config.getConfigurationSection(item.get().getType().toString());
		if(!c.isConfigurationSection(eventName))
			return null;
		
		return c.getConfigurationSection(eventName);
	}
	private void playerAction(ConfigurationSection subc, Player player) {
		if(subc == null)
			return;
		UniqueItem item = new UniqueItem(player.getItemInHand());
		int maxlevel = subc.getInt("MaxLevel", 1);
		Double rate = subc.getDouble("Rate",0.1);
		Double levelratefactor = subc.getDouble("LevelRateFactor",1);
		Double levelup = subc.getDouble("LevelupRequirement",10);
		
		String EnchantName = subc.getString("Enchant");
		if(EnchantName == null)
			return;
		System.out.println(this + " We have a working event!");
		Enchantment e = Enchantment.getByName(EnchantName);
		
		if(item.get().getEnchantmentLevel(e) >= maxlevel)
			return;
		
		if(!plugin.trackedItems.containsKey(player))
			plugin.trackedItems.put(player, new ItemStatus());
		
		if(!plugin.trackedItems.get(player).containsKey(item))
			plugin.trackedItems.get(player).put(item, new Double(0.0));
		
		Double trackedlevel = plugin.trackedItems.get(player).get(item);
		Double newlevel = new Double(trackedlevel + rate/(levelratefactor));
		if(newlevel >= levelup)
		{
			plugin.trackedItems.get(player).remove(item);
			item.get().addEnchantment(e, item.get().getEnchantmentLevel(e) + 1);
			plugin.trackedItems.get(player).put(item, new Double(0.0));
			player.sendMessage(ChatColor.GREEN + "Congrats you have leveled up an Item!");
		}
		else
		{
			plugin.trackedItems.get(player).put(item, newlevel);
		}
	}
	
}
