package com.precipicegames.autoenchanter.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import com.precipicegames.autoenchanter.Autoenchanter;


public class EntityLstn extends EntityListener {
	
	private Autoenchanter plugin;

	public EntityLstn(Autoenchanter p)
	{
		plugin = p;
	}
	public void onEntityDamage(EntityDamageEvent event) 
	{
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
			Player invloved;
			if(entityEvent.getDamager() instanceof Player && entityEvent.getEntity() instanceof LivingEntity) {
				invloved = (Player) entityEvent.getDamager();
				ConfigurationSection c = plugin.basicConfigurationHandler("DealDamageEvent", invloved , invloved.getItemInHand().getType());
				ConfigurationSection extended = null;
				for(Class<?> klass = event.getClass(); klass != null; klass = klass.getSuperclass()) {
					if(klass.getSimpleName().isEmpty()) {
						continue;
					}
					if(c.isConfigurationSection(klass.getSimpleName()))	{
						extended = c.getConfigurationSection(klass.getSimpleName());
						break;
					}
				}
				if(extended != null) {
					ConfigurationSection conf = new YamlConfiguration();
					for(String setting : c.getKeys(true)) {
						Object obj = c.get(setting);
						if(obj instanceof ConfigurationSection) {
							continue;
						}
						conf.set(setting, obj);
					}
					for(String setting : extended.getKeys(true)) {
						conf.set(setting, extended.get(setting));
					}
					plugin.basicActionHandler(conf, invloved, invloved.getItemInHand());
				}
				else {
					plugin.basicActionHandler(c, invloved, invloved.getItemInHand());
				}
			}
		}
	}
}
