package com.precipicegames.autoenchanter.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
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
		if(event.isCancelled()) {
			return;
		}
		ConfigurationSection subc = this.plugin.basicConfigurationHandler("BlockBreakEvent", event.getPlayer(), event.getPlayer().getItemInHand().getType());
		if(subc == null) {
			return;
		}
		ConfigurationSection extended = subc.getConfigurationSection(event.getBlock().getType().toString());
		if(extended != null) {
			ConfigurationSection conf = new YamlConfiguration();
			for(String setting : subc.getKeys(true)) {
				Object obj = subc.get(setting);
				if(obj instanceof ConfigurationSection)
					continue;
				conf.set(setting, obj);
			}
			for(String setting : extended.getKeys(true)) {
				conf.set(setting, extended.get(setting));
			}
			this.plugin.basicActionHandler(conf, event.getPlayer(), event.getPlayer().getItemInHand());
		}
		else {
			this.plugin.basicActionHandler(subc, event.getPlayer(), event.getPlayer().getItemInHand());
		}
	}
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled()) {
			return;
		}
		ConfigurationSection subc = this.plugin.basicConfigurationHandler("BlockPlaceEvent", event.getPlayer(), event.getPlayer().getItemInHand().getType());
		
		this.plugin.basicActionHandler(subc, event.getPlayer(), event.getPlayer().getItemInHand());
	}

}
