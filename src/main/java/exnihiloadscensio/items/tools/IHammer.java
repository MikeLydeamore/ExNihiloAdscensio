package exnihiloadscensio.items.tools;

import net.minecraft.item.ItemStack;

public interface IHammer {
	
	public boolean isHammer(ItemStack stack);
	
	public int getMiningLevel(ItemStack stack);

}
