package exnihiloadscensio.registries;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.util.ItemInfo;

public class SieveRegistry {

	private static HashMap<ItemInfo, ArrayList<Siftable>> registry = new HashMap<ItemInfo, ArrayList<Siftable>>();
	
	public static void register(ItemInfo block, ItemInfo drop, float chance, int meshLevel) {
		Siftable siftable = new Siftable(drop, chance, meshLevel);
		
		register(block, siftable);
	}
	
	public static void register(IBlockState state, ItemStack drop, float chance, int meshLevel) {
		register(new ItemInfo(state), new Siftable(new ItemInfo(drop), chance, meshLevel));
	}
	
	public static void register(ItemInfo block, Siftable drop) {
		ArrayList<Siftable> currentDrops;
		if (registry.containsKey(block)) {
			currentDrops = registry.get(block);
		} else {
			currentDrops = new ArrayList<Siftable>();
		}
		
		currentDrops.add(drop);
	}
	
	public static ArrayList<Siftable> getDrops(ItemInfo block) {
		if (!registry.containsKey(block))
			return null;
		
		return registry.get(block);
	}
	
	public static ArrayList<Siftable> getDrops(ItemStack block) {
		return getDrops(new ItemInfo(block));
	}
	
	public static boolean canBeSifted(ItemStack stack) {
		return registry.containsKey(new ItemInfo(stack));
	}
	
	public static void registerDefaults() {
		register(Blocks.GRAVEL.getDefaultState(), new ItemStack(Items.FLINT), 1f, MeshType.FLINT.getID());
	}
	
}
