package exnihiloadscensio.registries;

import java.util.ArrayList;
import java.util.HashMap;

import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.items.ItemResource;
import exnihiloadscensio.registries.types.CrookReward;
import exnihiloadscensio.util.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CrookRegistry {

	private static HashMap<BlockInfo, ArrayList<CrookReward>> map = new HashMap<BlockInfo, ArrayList<CrookReward>>();

	public static void addCrookRecipe(IBlockState state, ItemStack reward, float chance, float fortuneChance) {
		BlockInfo info = new BlockInfo(state);
		addCrookRecipe(info, reward, chance, fortuneChance);
	}

	public static void addCrookRecipe(BlockInfo info, ItemStack reward, float chance, float fortuneChance) {
		ArrayList<CrookReward> list = map.get(info);
		if (list == null)
		{
			list = new ArrayList<CrookReward>();
		}
		list.add(new CrookReward(reward, chance, fortuneChance));
		map.put(info, list);
	}

	public static boolean registered(Block block)
	{
		return map.containsKey(new BlockInfo(block.getDefaultState()));
	}

	public static ArrayList<CrookReward> getRewards(IBlockState state)
	{
		BlockInfo info = new BlockInfo(state);
		if (!map.containsKey(info))
			return null;

		return map.get(info);
	}

	public static void registerDefaults() {
		for (int i = 0 ; i < 16 ; i++) {
			addCrookRecipe(new BlockInfo(Blocks.LEAVES, i), ItemResource.getResourceStack(ItemResource.SILKWORM), 0.1f, 0f);
			addCrookRecipe(new BlockInfo(Blocks.LEAVES2, i), ItemResource.getResourceStack(ItemResource.SILKWORM), 0.1f, 0f);
		}
	}

}
