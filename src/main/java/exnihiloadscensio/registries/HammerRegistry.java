package exnihiloadscensio.registries;

import java.util.ArrayList;
import java.util.HashMap;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.util.BlockMeta;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HammerRegistry {
	
	private static HashMap<IBlockState, ArrayList<HammerReward>> map = new HashMap<IBlockState, ArrayList<HammerReward>>();
	
	/**
	 * Adds a new Hammer recipe for use with Ex Nihilo hammers.
	 * @param state Block State
	 * @param reward Reward
	 * @param miningLevel Mining level of hammer. 0 = Wood/Gold, 1 = Stone, 2 = Iron, 3 = Diamond. Can be higher, but will need corresponding tool material.
	 * @param chance Chance of drop
	 * @param fortuneChance Chance of drop per level of fortune
	 */
	public static void addHammerRecipe(IBlockState state, ItemStack reward, int miningLevel, float chance, float fortuneChance)
	{
		ArrayList<HammerReward> list = map.get(state);
		if (list == null)
		{
			list = new ArrayList<HammerReward>();
		}
		list.add(new HammerReward(reward, miningLevel, chance, fortuneChance));
		map.put(state, list);
	}
	
	public static ArrayList<HammerReward> getRewards(IBlockState state, int miningLevel)
	{
		if (!map.containsKey(state))
			return null;
		
		ArrayList<HammerReward> mapList = map.get(state);
		ArrayList<HammerReward> ret = new ArrayList<HammerReward>();
		for (HammerReward reward : mapList)
		{
			if (reward.getMiningLevel() <= miningLevel)
				ret.add(reward);
		}
		return ret;
	}
	
	public static boolean registered(Block block)
	{
		return map.containsKey(block.getDefaultState());
	}
	
	public static void addDefaultRecipes()
	{
		addHammerRecipe(Blocks.cobblestone.getDefaultState(), new ItemStack(Blocks.gravel), 0, 1f, 0f);
		addHammerRecipe(Blocks.gravel.getDefaultState(), new ItemStack(Blocks.sand), 0, 1f, 0f);
		addHammerRecipe(Blocks.sand.getDefaultState(), new ItemStack(ENBlocks.dust), 0, 1f, 0f);
	}

}
