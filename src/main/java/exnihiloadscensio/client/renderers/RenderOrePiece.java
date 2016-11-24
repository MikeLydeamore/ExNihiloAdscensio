package exnihiloadscensio.client.renderers;

import java.awt.Color;

import exnihiloadscensio.items.ore.ItemOre;
import exnihiloadscensio.items.ore.Ore;
import exnihiloadscensio.util.Util;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class RenderOrePiece implements IItemColor {

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if (stack == null)
			return 0;
		
		if (stack.getItem() instanceof ItemOre) {
			if (stack.getItemDamage() == 3 || stack.getItemDamage() == 2) {
				Ore ore = ((ItemOre) stack.getItem()).getOre();
				return ore.getColor().toInt();
			}
			if (tintIndex == 1) {
				Ore ore = ((ItemOre) stack.getItem()).getOre();
				return ore.getColor().toInt();
			}
		}
		return Color.WHITE.getRGB();
	}

}
