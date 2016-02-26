package exnihiloadscensio.registries;

import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import exnihiloadscensio.registries.types.Compostable;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.ItemInfo;

public class CompostRegistry {
	
	private static HashMap<ItemInfo, Compostable> registry = new HashMap<ItemInfo, Compostable>();
	
	public static void register(Item item, int meta, float value, Color color)
	{
		ItemInfo info = new ItemInfo(item, meta);
		Compostable compostable = new Compostable(value, color);
		
		registry.put(info, compostable);
	}
	
	public static boolean containsItem(Item item, int meta)
	{
		return containsItem(new ItemInfo(item, meta));
	}
	
	public static Compostable getItem(Item item, int meta)
	{
		return getItem(new ItemInfo(item, meta));
	}
	
	public static boolean containsItem(ItemStack stack)
	{
		return containsItem(new ItemInfo(stack));
	}
	
	public static Compostable getItem(ItemStack stack)
	{
		return getItem(new ItemInfo(stack));
	}
	
	public static boolean containsItem(ItemInfo info)
	{
		return registry.containsKey(info);
	}
	
	public static Compostable getItem(ItemInfo info)
	{
		return registry.get(info);
	}
	
	public static void registerDefaults()
	{
		register(Items.rotten_flesh, 0, 0.1f, new Color("C45631"));
		register(Item.getItemFromBlock(Blocks.sapling), 0, 0.125f, new Color("35A82A"));
	}

}
