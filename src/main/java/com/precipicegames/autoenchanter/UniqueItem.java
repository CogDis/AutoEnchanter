package com.precipicegames.autoenchanter;

import java.util.HashMap;

//import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class UniqueItem {
	private ItemStack stack;
	public UniqueItem(ItemStack s)
	{
		stack = s;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj.hashCode() == this.hashCode();
	}
	
	@Override
    public int hashCode() {
        int hash = 11;

        hash = hash * 19 + 7 * stack.getTypeId(); // Overriding hashCode since equals is overridden, it's just
        hash = hash * 7 + 23 * stack.getAmount(); // too bad these are mutable values... Q_Q
        System.out.println("DATA: " + stack.getTypeId() + " : " + stack.getAmount() + " : " + hash);
        
        for(Enchantment e : stack.getEnchantments().keySet())
        {
        	hash = hash * 29 + 3 * e.hashCode();
        	hash = hash * 11 + 17 * stack.getEnchantmentLevel(e);
        }
        return hash;
    }
    public ItemStack get()
    {
    	return stack;
    }
    public class ItemStatus extends HashMap<UniqueItem, Double> {
		private static final long serialVersionUID = -2359476030780896070L;};
}
