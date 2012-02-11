package com.precipicegames.autoenchanter.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.precipicegames.autoenchanter.Autoenchanter;


public class EntityLstn implements Listener {
	
	private final Autoenchanter plugin;

	public EntityLstn(Autoenchanter p)
	{
		plugin = p;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageEvent event) 
	{
		if(event.isCancelled())
			return;
		if(event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
			Entity dealer = entityEvent.getDamager();
			if(dealer instanceof Projectile) {
				dealer = ((Projectile) dealer).getShooter();
			}
			if(dealer instanceof Player && entityEvent.getEntity() instanceof LivingEntity) {
				Player invloved = (Player) dealer;
				ConfigurationSection c = plugin.basicConfigurationHandler("DealDamageEvent", invloved , invloved.getItemInHand().getType());
				if(c == null) {
					return;
				}
				ConfigurationSection extended = null;
				for(Class<?> klass = event.getEntity().getClass(); klass != null; klass = klass.getSuperclass()) {
					if(klass.getSimpleName().isEmpty()) {
						continue;
					}
					if(plugin.debug) {
						System.out.println(this + "[DEBUG] searching config for " + klass.getSimpleName());
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
