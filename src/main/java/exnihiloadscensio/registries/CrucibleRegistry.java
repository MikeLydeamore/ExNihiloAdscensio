package exnihiloadscensio.registries;

import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import exnihiloadscensio.registries.types.Meltable;
import exnihiloadscensio.util.ItemInfo;

public class CrucibleRegistry {
	
	private static HashMap<ItemInfo, Meltable> registry = new HashMap<ItemInfo, Meltable>();
	
	public static void register(ItemInfo item, Fluid fluid, int amount) {
		Meltable meltable = new Meltable(fluid.getName(), amount);
		
		registry.put(item, meltable);
	}
	
	public static void register(ItemStack stack, Fluid fluid, int amount) {
		register(new ItemInfo(stack), fluid, amount);
	}
	
	public static boolean canBeMelted(ItemStack stack) {
		ItemInfo info = new ItemInfo(stack);
		
		return registry.containsKey(info);
	}
	
	public static Meltable getMeltable(ItemStack stack) {
		ItemInfo info = new ItemInfo(stack);
		
		return registry.get(info);
	}
	
	public static Meltable getMeltable(ItemInfo info) {
		return registry.get(info);
	}
	
	public static void registerDefaults() {
		register(new ItemStack(Blocks.COBBLESTONE), FluidRegistry.LAVA, 250);
	}

}
