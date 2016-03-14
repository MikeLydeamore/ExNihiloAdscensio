package exnihiloadscensio.registries;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import exnihiloadscensio.util.BlockMeta;

public class CrookRegistry {
	
	private static HashMap<IBlockState, ArrayList<CrookReward>> map = new HashMap<IBlockState, ArrayList<CrookReward>>();
	
	public static void addCrookRecipe(IBlockState state, ItemStack reward, float chance, float fortuneChance)
	{
		ArrayList<CrookReward> list = map.get(state);
		if (list == null)
		{
			list = new ArrayList<CrookReward>();
		}
		list.add(new CrookReward(reward, chance, fortuneChance));
		map.put(state, list);
	}
	
	public static boolean registered(Block block)
	{
		return map.containsKey(block.getDefaultState());
	}
	
	public static ArrayList<CrookReward> getRewards(IBlockState state)
	{
		if (!map.containsKey(state))
			return null;
		
		return map.get(state);
	}

}
