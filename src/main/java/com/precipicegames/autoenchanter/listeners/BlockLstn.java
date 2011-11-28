package com.precipicegames.autoenchanter.listeners;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.precipicegames.autoenchanter.Autoenchanter;

public class BlockLstn extends BlockListener {
	private Autoenchanter plugin;
	public BlockLstn(Autoenchanter p)
	{
		plugin = p;
	}
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.isCancelled())
			return;
		ConfigurationSection subc = this.plugin.basicConfigurationHandler("BlockBreakEvent", event.getPlayer(), event.getPlayer().getItemInHand().getType());
		ConfigurationSection extended = subc.getConfigurationSection(event.getBlock().getType().toString());
		if(extended != null)	
		{
			Configuration conf = new MemoryConfiguration();
			for(String setting : subc.getKeys(true))
			{
				conf.set(setting, subc.get(setting));
			}
			for(String setting : extended.getKeys(true))
			{
				conf.set(setting, extended.get(setting));
			}
			this.plugin.basicActionHandler(conf, event.getPlayer(), event.getPlayer().getItemInHand());
		}
		else
		{
			this.plugin.basicActionHandler(subc, event.getPlayer(), event.getPlayer().getItemInHand());
		}
	}
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled())
			return;
		ConfigurationSection subc = this.plugin.basicConfigurationHandler("BlockPlaceEvent", event.getPlayer(), event.getPlayer().getItemInHand().getType());
		
		this.plugin.basicActionHandler(subc, event.getPlayer(), event.getPlayer().getItemInHand());
	}

}
