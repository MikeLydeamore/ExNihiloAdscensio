package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.Compostable;
import exnihiloadscensio.util.ItemInfo;

public class HeatRegistry {
	
	private static HashMap<ItemInfo, Integer> registry = new HashMap<ItemInfo, Integer>();
	
	private static Gson gson;
	
	public static void register(ItemInfo info, int ticksBetween) {
		registry.put(info, ticksBetween);
	}
	
	public static void register(ItemStack stack, int ticksBetween) {
		register(new ItemInfo(stack), ticksBetween);
	}
	
	public static void registerDefaults() {
		for (int i = 0 ; i < 16; i++)
			register(new ItemStack(Blocks.TORCH, 1, i), 1);
		for (int i = 0 ; i < 16; i++)
			register(new ItemInfo(Blocks.LAVA, i), 3);
		for (int i = 0 ; i < 16; i++)
			register(new ItemInfo(Blocks.FLOWING_LAVA, 1), 2);
	}
	
	public static int getHeatAmount(ItemStack stack) {
		return registry.get(new ItemInfo(stack));
	}
	
	public static int getHeatAmount(ItemInfo info) {
		if (registry.containsKey(info))
			return registry.get(info);
		
		return 0;
	}
	
	public static void loadJson(File file) {
		gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson()).create();
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
					ItemInfo stack = new ItemInfo(s);
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
