package com.precipicegames.autoenchanter.listeners;

import org.bukkit.configuration.ConfigurationSection;
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
		
		this.plugin.basicActionHandler(subc, event.getPlayer(), event.getPlayer().getItemInHand());
	}
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled())
			return;
		ConfigurationSection subc = this.plugin.basicConfigurationHandler("BlockPlaceEvent", event.getPlayer(), event.getPlayer().getItemInHand().getType());
		
		this.plugin.basicActionHandler(subc, event.getPlayer(), event.getPlayer().getItemInHand());
	}

}
