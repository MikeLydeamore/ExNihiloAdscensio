package exnihiloadscensio.items.tools;

import net.minecraft.item.ItemStack;

public interface ICrook {
	
	public boolean isCrook(ItemStack stack);
	
	public double getDropModifier(ItemStack stack);

}
