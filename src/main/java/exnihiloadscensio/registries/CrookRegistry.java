package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	private static Map<BlockInfo, List<CrookReward>> registry = new HashMap<>();
	private static Map<BlockInfo, List<CrookReward>> externalRegistry = new HashMap<>();
	
	// Why you so inconsistent?
	@Deprecated
	/*
	 * Call register instead
	 */
	public static void addCrookRecipe(IBlockState state, ItemStack reward, float chance, float fortuneChance)
	{
		register(new BlockInfo(state), reward, chance, fortuneChance);
	}
	
	@Deprecated
    /*
     * Call register instead
     */
	public static void addCrookRecipe(BlockInfo info, ItemStack reward, float chance, float fortuneChance)
	{
	    register(info, reward, chance, fortuneChance);
	}
	
	public static void register(BlockInfo info, ItemStack reward, float chance, float fortuneChance)
	{
	    registerInternal(info, reward, chance, fortuneChance);
	    
	    List<CrookReward> list = externalRegistry.get(info);
        
        if (list == null)
        {
            list = new ArrayList<>();
        }
        
        list.add(new CrookReward(reward, chance, fortuneChance));
        externalRegistry.put(info, list);
	}
	
	private static void registerInternal(BlockInfo info, ItemStack reward, float chance, float fortuneChance)
	{
	    List<CrookReward> list = registry.get(info);
        
        if (list == null)
        {
            list = new ArrayList<>();
        }
        
        list.add(new CrookReward(reward, chance, fortuneChance));
        registry.put(info, list);
	}

	public static boolean registered(Block block)
	{
		return registry.containsKey(new BlockInfo(block.getDefaultState()));
	}
	
	public static ArrayList<CrookReward> getRewards(IBlockState state)
	{
		BlockInfo info = new BlockInfo(state);
		if (!registry.containsKey(info))
			return null;

		return (ArrayList<CrookReward>) registry.get(info);
	}

	public static void registerDefaults()
    {
        registerInternal(new BlockInfo(Blocks.LEAVES, -1), ItemResource.getResourceStack(ItemResource.SILKWORM), 0.1f, 0f);
        registerInternal(new BlockInfo(Blocks.LEAVES2, -1), ItemResource.getResourceStack(ItemResource.SILKWORM), 0.1f, 0f);
    }
    
    private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemStack.class, new CustomItemStackJson()).registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson()).create();
    
	public static void loadJson(File file)
    {
        registry.clear();
        
        if (file.exists())
        {
            try
            {
                FileReader fr = new FileReader(file);
                HashMap<String, ArrayList<CrookReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<CrookReward>>>()
                {
                }.getType());
                
                Iterator<String> it = gsonInput.keySet().iterator();
                
                while (it.hasNext())
                {
                    String s = (String) it.next();
                    BlockInfo stack = new BlockInfo(s);
                    registry.put(stack, gsonInput.get(s));
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

			gson.toJson(registry, fw);
			
			fw.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
