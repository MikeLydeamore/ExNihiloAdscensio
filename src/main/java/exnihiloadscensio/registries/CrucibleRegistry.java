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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.Compostable;
import exnihiloadscensio.registries.types.Meltable;
import exnihiloadscensio.util.ItemInfo;

public class CrucibleRegistry {
	
	private static HashMap<ItemInfo, Meltable> registry = new HashMap<ItemInfo, Meltable>();
	
	private static Gson gson;
	
	public static void register(ItemInfo item, Fluid fluid, int amount) {
		Meltable meltable = new Meltable(fluid.getName(), amount);
		
		registry.put(item, meltable);
	}
	
	public static void register(ItemStack stack, Fluid fluid, int amount) {
		register(new ItemInfo(stack), fluid, amount);
	}
	
	public static void register(ItemInfo item, Meltable meltable) {
		registry.put(item, meltable);
	}
	
	public static boolean canBeMelted(ItemStack stack) {
		ItemInfo info = new ItemInfo(stack);
		
		return registry.containsKey(info);
	}
	
	public static Meltable getMeltable(ItemStack stack) {
		ItemInfo info = new ItemInfo(stack);
		
		return registry.get(info);
	}
	
	public static Meltable getMeltable(ItemInfo info) {
		return registry.get(info);
	}
	
	public static void registerDefaults() {
		register(new ItemStack(Blocks.COBBLESTONE), FluidRegistry.LAVA, 250);
	}
	
	public static void loadJson(File file)
	{
		gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson()).create();
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				HashMap<String, Meltable> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, Meltable>>(){}.getType());
				
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
