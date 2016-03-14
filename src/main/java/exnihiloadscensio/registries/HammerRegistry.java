package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.json.CustomItemStackJson;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HammerRegistry {
	
	private static HashMap<ItemInfo, ArrayList<HammerReward>> map = new HashMap<ItemInfo, ArrayList<HammerReward>>();
	
	private static Gson gson = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(ItemStack.class, new CustomItemStackJson())
			.create();

	public static void loadJson(File file)
	{
		
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				HashMap<String, ArrayList<HammerReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<HammerReward>>>(){}.getType());
				
				Iterator<String> it = gsonInput.keySet().iterator();
				
				while (it.hasNext())
				{
					String s = (String) it.next();
					ItemInfo stack = new ItemInfo(s);
					map.put(stack, gsonInput.get(s));
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			registerDefaults();
			saveJson(file);
		}
	}
	
	public static void saveJson(File file)
	{
		try
		{
			FileWriter fw = new FileWriter(file);

			gson.toJson(map, fw);
			
			fw.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
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
		ItemInfo ii = new ItemInfo(state);
		map.put(ii, list);
	}
	
	public static ArrayList<HammerReward> getRewards(IBlockState state, int miningLevel)
	{
		ItemInfo ii = new ItemInfo(state);
		if (!map.containsKey(ii))
			return null;
		
		ArrayList<HammerReward> mapList = map.get(ii);
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
		ItemInfo ii = new ItemInfo(block.getDefaultState());
		return map.containsKey(ii);
	}
	
	public static void registerDefaults()
	{
		addHammerRecipe(Blocks.cobblestone.getDefaultState(), new ItemStack(Blocks.gravel), 0, 1f, 0f);
		addHammerRecipe(Blocks.gravel.getDefaultState(), new ItemStack(Blocks.sand), 0, 1f, 0f);
		addHammerRecipe(Blocks.sand.getDefaultState(), new ItemStack(ENBlocks.dust), 0, 1f, 0f);
	}


}
