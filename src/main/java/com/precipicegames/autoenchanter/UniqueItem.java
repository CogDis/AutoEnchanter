package com.precipicegames.autoenchanter;

import java.util.HashMap;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class UniqueItem {
	private ItemStack stack;
	public UniqueItem(ItemStack s)
	{
		stack = s;
	}
    public int hashCode() {
        int hash = 11;

        hash = hash * 19 + 7 * stack.getTypeId(); // Overriding hashCode since equals is overridden, it's just
        hash = hash * 7 + 23 * stack.getAmount(); // too bad these are mutable values... Q_Q
        for(Enchantment e : stack.getEnchantments().keySet())
        {
        	hash = hash ^ e.hashCode();
        	hash = hash * (11 + 17 * stack.getEnchantmentLevel(e));
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
