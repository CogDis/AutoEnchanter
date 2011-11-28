package com.precipicegames.autoenchanter.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import com.precipicegames.autoenchanter.Autoenchanter;


public class EntityLstn extends EntityListener {
	private Autoenchanter plugin;

	EntityLstn(Autoenchanter p)
	{
		plugin = p;
	}
	public void onEntityDamage(EntityDamageEvent event) 
	{
		if(event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
			Player invloved;
			if(entityEvent.getDamager() instanceof Player && entityEvent.getEntity() instanceof LivingEntity)
			{
				invloved = (Player) entityEvent.getDamager();
				ConfigurationSection c = plugin.basicConfigurationHandler("DealDamageEvent", invloved , invloved.getItemInHand().getType());
				plugin.basicActionHandler(c, invloved, invloved.getItemInHand());
			}
		}
	}
}
