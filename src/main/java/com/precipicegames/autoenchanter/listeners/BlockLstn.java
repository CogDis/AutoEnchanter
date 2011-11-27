package com.precipicegames.autoenchanter.listeners;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.precipicegames.autoenchanter.Autoenchanter;
import com.precipicegames.autoenchanter.UniqueItem;
import com.precipicegames.autoenchanter.ItemStatus;

public class BlockLstn extends BlockListener {
	private Autoenchanter plugin;
	public BlockLstn(Autoenchanter p)
	{
		plugin = p;
	}
	public ConfigurationSection blockEventConfig(String eventName, Player p ,BlockEvent event)
	{
		UniqueItem item = new UniqueItem(p.getItemInHand());
		System.out.println(this + " fired event!");
		if(!plugin.config.isConfigurationSection(item.get().getType().toString()))
		{
			System.out.println(this + "Not found "+item.get().getType().toString());
			return null;
		}
		
		ConfigurationSection c = plugin.config.getConfigurationSection(item.get().getType().toString());
		System.out.println(this + " found block definition");
		if(!c.isConfigurationSection(eventName))
			return null;
		
		return c.getConfigurationSection(eventName);
	}
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled())
			return;
		ConfigurationSection subc = this.blockEventConfig("BlockBreakEvent", event.getPlayer(), event);
		
		this.blockAction(subc, event.getPlayer());
	}
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled())
			return;
		ConfigurationSection subc = this.blockEventConfig("BlockPlaceEvent", event.getPlayer(), event);
		this.blockAction(subc, event.getPlayer());
	}
	
	private void blockAction(ConfigurationSection subc, Player player) {
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
