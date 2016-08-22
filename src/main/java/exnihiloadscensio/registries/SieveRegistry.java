package exnihiloadscensio.registries;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.items.ItemResource;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;

public class SieveRegistry {

	private static HashMap<BlockInfo, ArrayList<Siftable>> registry = new HashMap<BlockInfo, ArrayList<Siftable>>();
	
	public static void register(BlockInfo block, ItemInfo drop, float chance, int meshLevel) {
		Siftable siftable = new Siftable(drop, chance, meshLevel);
		
		register(block, siftable);
	}
	
	public static void register(IBlockState state, ItemStack drop, float chance, int meshLevel) {
		register(new BlockInfo(state), new Siftable(new ItemInfo(drop), chance, meshLevel));
	}
	
	public static void register(BlockInfo block, Siftable drop) {
		ArrayList<Siftable> currentDrops;
		if (registry.containsKey(block)) {
			currentDrops = registry.get(block);
		} else {
			currentDrops = new ArrayList<Siftable>();
		}
		
		currentDrops.add(drop);
		registry.put(block, currentDrops);
	}
	
	/**
	 * Gets *all* possible drops from the sieve. It is up to the dropper to
	 * check whether or not the drops should be dropped!
	 * @param block
	 * @return ArrayList of {@linkplain exnihiloadscensio.registries.types.Siftable}
	 * that could *potentially* be dropped.
	 */
	public static ArrayList<Siftable> getDrops(BlockInfo block) {
		if (!registry.containsKey(block))
			return null;
		
		return registry.get(block);
	}
	
	/**
	 * Gets *all* possible drops from the sieve. It is up to the dropper to
	 * check whether or not the drops should be dropped!
	 * @param block
	 * @return ArrayList of {@linkplain exnihiloadscensio.registries.types.Siftable}
	 * that could *potentially* be dropped.
	 */
	public static ArrayList<Siftable> getDrops(ItemStack block) {
		return getDrops(new BlockInfo(block));
	}
	
	public static boolean canBeSifted(ItemStack stack) {
		return registry.containsKey(new ItemInfo(stack));
	}
	
	public static void registerDefaults() {
		register(Blocks.DIRT.getDefaultState(), ItemResource.getResourceStack("stones"), 1f, MeshType.STRING.getID());
		register(Blocks.DIRT.getDefaultState(), ItemResource.getResourceStack("stones"), 1f, MeshType.STRING.getID());
		register(Blocks.DIRT.getDefaultState(), ItemResource.getResourceStack("stones"), 0.5f, MeshType.STRING.getID());
		register(Blocks.DIRT.getDefaultState(), ItemResource.getResourceStack("stones"), 0.25f, MeshType.STRING.getID());
		register(Blocks.GRAVEL.getDefaultState(), new ItemStack(Items.FLINT), 1f, MeshType.FLINT.getID());
	}
	
}
