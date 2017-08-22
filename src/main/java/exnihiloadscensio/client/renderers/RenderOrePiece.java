package exnihiloadscensio.client.renderers;

import exnihiloadscensio.items.ore.ItemOre;
import exnihiloadscensio.items.ore.Ore;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;

public class RenderOrePiece implements IItemColor {

	@Override
	public int getColorFromItemstack(@Nonnull ItemStack stack, int tintIndex) {
		if (stack.isEmpty())
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
