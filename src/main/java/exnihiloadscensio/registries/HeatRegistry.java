package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.json.CustomBlockInfoJson;
import exnihiloadscensio.util.BlockInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HeatRegistry {
	
	private static HashMap<BlockInfo, Integer> registry = new HashMap<BlockInfo, Integer>();
	
	private static Gson gson;
	
	public static void register(BlockInfo info, int ticksBetween) {
		registry.put(info, ticksBetween);
	}
	
	public static void register(ItemStack stack, int ticksBetween) {
		register(new BlockInfo(stack), ticksBetween);
	}
	
	public static void registerDefaults() {
		for (int i = 0 ; i < 16; i++)
			register(new ItemStack(Blocks.TORCH, 1, i), 1);
		for (int i = 0 ; i < 16; i++)
			register(new BlockInfo(Blocks.LAVA, i), 3);
		for (int i = 0 ; i < 16; i++)
			register(new BlockInfo(Blocks.FLOWING_LAVA, i), 2);
		for (int i = 0 ; i < 16; i++)
			register(new BlockInfo(Blocks.FIRE, i), 4);
	}
	
	public static int getHeatAmount(ItemStack stack) {
		return registry.get(new BlockInfo(stack));
	}
	
	public static int getHeatAmount(BlockInfo info) {
		if (registry.containsKey(info))
			return registry.get(info);
		
		return 0;
	}
	
	public static void loadJson(File file) {
        registry.clear();

		gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson()).create();
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				HashMap<String, Integer> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, Integer>>(){}.getType());
				
                Iterator<String> it = gsonInput.keySet().iterator();
                
				while (it.hasNext())
				{
					String s = (String) it.next();
					BlockInfo stack = new BlockInfo(s);
					register(stack, gsonInput.get(s));
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
	
	public static void saveJson(File file) {
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
