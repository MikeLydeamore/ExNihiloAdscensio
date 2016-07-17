package exnihiloadscensio.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
	
	public ItemInfo(String string)
	{
		String[] arr = string.split(":");
		item = Item.REGISTRY.getObject(new ResourceLocation(arr[0]+":"+arr[1]));
		meta = Integer.parseInt(arr[2]);
	}
	
	public ItemInfo(IBlockState state)
	{
		this.item = Item.getItemFromBlock(state.getBlock());
		this.meta = state.getBlock().getMetaFromState(state);
	}
	
	public String toString()
	{
		return Item.REGISTRY.getNameForObject(item)+":"+meta;
	}
	
	public ItemStack getItemStack() {
		return new ItemStack(this.item, 1, this.meta);
	}

}
