package exnihiloadscensio.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor @EqualsAndHashCode
public class ItemInfo {
	
	@Getter
	private Item item;
	@Getter
	private int meta;
	
	public static ItemInfo getItemInfoFromStack(ItemStack stack)
	{
		return new ItemInfo(stack.getItem(), stack.getItemDamage());
	}

	public ItemInfo(ItemStack stack)
	{
		item = stack.getItem();
		meta = stack.getItemDamage();
	}

}
