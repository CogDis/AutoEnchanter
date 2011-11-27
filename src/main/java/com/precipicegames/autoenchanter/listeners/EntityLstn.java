package com.precipicegames.autoenchanter.listeners;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.precipicegames.autoenchanter.Autoenchanter;
import com.precipicegames.autoenchanter.ItemStatus;
import com.precipicegames.autoenchanter.UniqueItem;

public class EntityLstn extends EntityListener {
	private Autoenchanter plugin;

	EntityLstn(Autoenchanter p)
	{
		plugin = p;
	}
	public void onProjectileHit(ProjectileHitEvent event)
	{
		
	}
	
	private void entityAction(ConfigurationSection subc, Player player) {
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
