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

import exnihiloadscensio.items.ItemResource;
import exnihiloadscensio.json.CustomBlockInfoJson;
import exnihiloadscensio.json.CustomItemStackJson;
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
	
	private static Gson gson = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(ItemStack.class, new CustomItemStackJson())
			.registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson())
			.create();

	public static void loadJson(File file)	{
		
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				HashMap<String, ArrayList<CrookReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<CrookReward>>>(){}.getType());
				
				Iterator<String> it = gsonInput.keySet().iterator();
				
				while (it.hasNext())
				{
					String s = (String) it.next();
					BlockInfo stack = new BlockInfo(s);
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

}
