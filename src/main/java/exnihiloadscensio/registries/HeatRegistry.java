package exnihiloadscensio.registries;

import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import exnihiloadscensio.util.ItemInfo;

public class HeatRegistry {
	
	private static HashMap<ItemInfo, Integer> registry = new HashMap<ItemInfo, Integer>();
	
	public static void register(ItemInfo info, int ticksBetween) {
		registry.put(info, ticksBetween);
	}
	
	public static void register(ItemStack stack, int ticksBetween) {
		register(new ItemInfo(stack), ticksBetween);
	}
	
	public static void registerDefaults() {
		for (int i = 0 ; i < 16; i++)
			register(new ItemStack(Blocks.TORCH, 1, i), 1);
	}
	
	public static int getHeatAmount(ItemStack stack) {
		return registry.get(new ItemInfo(stack));
	}
	
	public static int getHeatAmount(ItemInfo info) {
		if (registry.containsKey(info))
			return registry.get(info);
		
		return 0;
	}

}
